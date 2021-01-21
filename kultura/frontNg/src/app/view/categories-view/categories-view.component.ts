import {Component, OnDestroy, OnInit} from '@angular/core';
import {CategoryService} from '../../core/services/category/category.service';
import {Observable, Subscription} from 'rxjs';
import {MessageService} from 'primeng/api';
import {Category} from '../../core/models/category';
import {FormControl, Validators} from '@angular/forms';
import {DialogService} from 'primeng/dynamicdialog';
import {SubcategoriesViewComponent} from '../subcategories-view/subcategories-view.component';

@Component({
  selector: 'app-categories-view',
  templateUrl: './categories-view.component.html',
  styleUrls: ['./categories-view.component.scss']
})
export class CategoriesViewComponent implements OnInit, OnDestroy {

  private subscriptions: Subscription[] = [];

  page = -1;
  totalPages = 0;
  isCategoriesLoading = false;

  isAddDialogOpen = false;
  nameControl = new FormControl('', [Validators.required]);
  editingCategory?: Category;

  constructor(private categoryService: CategoryService,
              private messageService: MessageService,
              private dialogService: DialogService) {
  }

  ngOnInit(): void {
    this.resetCategories();
  }

  resetCategories(): void {
    this.isCategoriesLoading = true;
    this.categoryService.categories = [];
    this.page = -1;
    this.totalPages = 0;
    this.nameControl.reset();
    this.getCategories();
  }

  onScrollDown(): void {
    this.getCategories();
  }

  getCategories(): void {
    if (this.page === this.totalPages) {
      this.isCategoriesLoading = false;
      return;
    }
    this.isCategoriesLoading = true;
    this.subscriptions.push(
      this.categoryService.getCategories(this.page + 1).subscribe(
        val => {
          for (const el of val.content) {
            if (this.categoryService.categories.some(mod => mod.id === el.id)) {
              continue;
            }
            this.categoryService.categories.push(el);
          }
          this.page = val.pageable.pageNumber;
          this.totalPages = val.totalPages;
          this.isCategoriesLoading = false;
        }
      )
    );
  }

  openAddDialog(editing = false, category?: Category): void {
    this.isAddDialogOpen = true;
    if (editing) {
      this.nameControl.setValue(category?.name ?? '');
      this.editingCategory = category;
    }
  }

  openSubcategoriesDialog(category: Category): void {
    this.dialogService.open(SubcategoriesViewComponent, {
      header: 'Subcategories',
      modal: true,
      data: {category},
      baseZIndex: 2000,
    });
  }

  onHideAddDialog(): void {
    this.nameControl.reset();
    this.editingCategory = undefined;
  }

  saveCategory(): void {
    if (!this.nameControl.valid) {
      this.messageService.add(
        {id: 'toast-container', severity: 'error', summary: 'Required', detail: 'Name is required.'}
      );
    }
    const name = this.nameControl.value;
    let c: Category;
    if (!!this.editingCategory) {
      c = this.editingCategory;
    } else {
      c = new Category();
    }
    c.name = name;
    let o: Observable<any>;

    if (!!this.editingCategory) {
      o = this.categoryService.update(c);
    } else {
      o = this.categoryService.create(c);
    }
    this.subscriptions.push(
      o.subscribe(() => {
        this.messageService.add(
          {id: 'toast-container', severity: 'success', summary: 'Created', detail: 'Category was created.'}
        );
        this.resetCategories();
        this.isAddDialogOpen = false;
        this.nameControl.reset();
        this.editingCategory = undefined;
      },
      () => {
        this.messageService.add(
          {id: 'toast-container', severity: 'error', summary: 'Already exists', detail: 'A category with this name already exists'}
        );
        this.isAddDialogOpen = false;
        this.nameControl.reset();
      })
    );
  }

  categoryDeletionConfirmed(): void {
    this.resetCategories();
  }

  get categories(): Category[] {
    return this.categoryService.categories;
  }

  get editing(): boolean {
    return !!this.editingCategory;
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach(s => s.unsubscribe());
  }
}
