import {Component, OnDestroy, OnInit} from '@angular/core';
import {CategoryService} from '../../core/services/category/category.service';
import {Subscription} from 'rxjs';
import {MessageService} from 'primeng/api';
import {Category} from '../../core/models/category';

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

  constructor(private categoryService: CategoryService,
              private messageService: MessageService) {
  }

  ngOnInit(): void {
    this.resetCategories();
  }

  resetCategories(): void {
    this.isCategoriesLoading = true;
    this.categoryService.categories = [];
    this.page = -1;
    this.totalPages = 0;
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

  categoryDeletionConfirmed(): void {
    this.resetCategories();
  }

  get categories(): Category[] {
    return this.categoryService.categories;
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach(s => s.unsubscribe());
  }
}
