import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Category} from '../../core/models/category';
import {Router} from '@angular/router';
import {ConfirmationService, MessageService} from 'primeng/api';
import {CategoryService} from '../../core/services/category/category.service';
import {Subscription} from 'rxjs';

@Component({
  selector: 'app-category-list-item',
  templateUrl: './category-list-item.component.html',
  styleUrls: ['./category-list-item.component.scss']
})
export class CategoryListItemComponent implements OnInit {
  private subscriptions: Subscription[] = [];

  @Input()
  category!: Category;
  @Output()
  categoryDeleted: EventEmitter<any> = new EventEmitter<any>();

  constructor(private router: Router,
              private categoryService: CategoryService,
              private messageService: MessageService,
              private confirmationService: ConfirmationService) { }

  ngOnInit(): void {
  }

  getThumbnailUrl(): void {
  }

  onClickDelete(): void {
    this.confirmationService.confirm(
      {
        message: `Are you sure that you want to delete ${this.category.name ?? ''}`,
        acceptLabel: 'Delete',
        rejectLabel: 'Close',
        header: 'Deletion',
        icon: 'pi pi-trash',
        accept: () => this.deletionConfirmed()
      });
  }

  deletionConfirmed(): void {
    this.subscriptions.push(
      this.categoryService.delete(this.category?.id ?? 0).subscribe(() => {
        this.messageService.add({
          severity: 'success',
          summary: 'Deleted successfully',
          detail: 'The category was deleted successfully'
        });
        this.categoryDeleted.emit(this.category);
      },
        er => {
          this.messageService.add({
            severity: 'error',
            summary: 'Deletion unsuccessful',
            detail: 'This category has subcategories associated with it. Firstly delete all its subcategories in order to be able to delete it.'
          });
        })
    );
  }

  onClickEdit(): void{
  }

}
