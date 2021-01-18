import {Component, ElementRef, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {DynamicDialogConfig, DynamicDialogRef} from 'primeng/dynamicdialog';
import {Category} from '../../core/models/category';
import {CategoryService} from '../../core/services/category/category.service';
import {Subcategory} from '../../core/models/subcategory';
import {FormControl, Validators} from '@angular/forms';
import {ConfirmationService, LazyLoadEvent, MessageService} from 'primeng/api';
import {Observable, Subscription} from 'rxjs';
import {Table} from 'primeng/table';

@Component({
  selector: 'app-subcategories-view',
  templateUrl: './subcategories-view.component.html',
  styleUrls: ['./subcategories-view.component.scss']
})
export class SubcategoriesViewComponent implements OnInit, OnDestroy {

  private subscriptions: Subscription[] = [];

  @ViewChild('dt')
  table!: Table;

  category?: Category;
  subcategories: Subcategory[] = [];
  page = 0;
  totalPages = 0;
  totalElements = 0;
  isSubcategoryDialogOpen = false;
  nameControl = new FormControl('', [Validators.required]);
  editingSubcategory?: Subcategory;
  isSubcategoriesLoading = true;

  constructor(private ref: DynamicDialogRef,
              private config: DynamicDialogConfig,
              private categoryService: CategoryService,
              private messageService: MessageService,
              private confirmationService: ConfirmationService) { }

  ngOnInit(): void {
    this.category = this.config.data.category;
  }

  getSubcategories(event: LazyLoadEvent): void {
    this.isSubcategoriesLoading = true;
    const page = Math.floor((event.first ?? 0) / (event.rows ?? -1));
    this.categoryService.getSubcategories(this.category?.id ?? 0, page).subscribe(val => {
      this.subcategories = val.content;
      this.totalElements = val.totalElements;
      this.isSubcategoriesLoading = false;
    });
  }

  openNew(): void {
    this.isSubcategoryDialogOpen = true;
  }

  editSubcategory(subcategory: Subcategory): void {
    this.editingSubcategory = subcategory;
    this.isSubcategoryDialogOpen = true;
    this.nameControl.setValue(subcategory?.name ?? '');
  }

  deleteSubcategory(subcategory: Subcategory): void {
    this.confirmationService.confirm(
      {
        message: `Are you sure that you want to delete ${subcategory.name ?? ''}`,
        acceptLabel: 'Delete',
        rejectLabel: 'Close',
        header: 'Deletion',
        icon: 'pi pi-trash',
        accept: () => this.deletionConfirmed(subcategory)
      });
  }

  deletionConfirmed(subcategory: Subcategory): void {
    this.subscriptions.push(
      this.categoryService.deleteSubcategory(subcategory?.id ?? 0).subscribe(() => {
          this.messageService.add({
            severity: 'success',
            summary: 'Deleted successfully',
            detail: 'The category was deleted successfully'
          });
          this.resetSubcategories();
        },
        er => {
          this.messageService.add({
            severity: 'error',
            summary: 'Deletion unsuccessful',
            detail: 'This subcategory has offerings associated with it. Firstly delete all its offerings in order to be able to delete it.'
          });
        })
    );
  }

  onHideSubcategoryDialog(): void {
  }

  resetSubcategories(): void {
    this.table.reset();
  }

  saveSubcategory(): void {
    if (!this.nameControl.valid) {
      this.messageService.add(
        {severity: 'error', summary: 'Required', detail: 'Name is required.'}
      );
    }
    const name = this.nameControl.value;
    let s: Subcategory;
    if (!!this.editingSubcategory) {
      s = this.editingSubcategory;
    } else {
      s = new Subcategory();
    }
    s.name = name;
    s.categoryId = this.category?.id ?? -1;
    let o: Observable<any>;

    if (!!this.editingSubcategory) {
      o = this.categoryService.updateSubcategory(s);
    } else {
      o = this.categoryService.createSubcategory(s);
    }
    this.subscriptions.push(
      o.subscribe(() => {
          this.messageService.add(
            {severity: 'success', summary: 'Saved', detail: 'Subcategory was successfully saved.'}
          );
          this.resetSubcategories();
          this.isSubcategoryDialogOpen = false;
          this.nameControl.reset();
          if (!this.editingSubcategory && this.category && this.category.numSubcategories) {
            this.category.numSubcategories++;
          }
          this.editingSubcategory = undefined;

        },
        () => {
          this.messageService.add(
            {severity: 'error', summary: 'Already exists', detail: 'A category with this name already exists'}
          );
          this.isSubcategoryDialogOpen = false;
          this.nameControl.reset();
        })
    );
  }

  get editing(): boolean {
    return !!this.editingSubcategory;
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach(s => s.unsubscribe());
  }
}
