import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Category} from '../../core/models/category';
import {Router} from '@angular/router';
import {ConfirmationService, MessageService} from 'primeng/api';
import {CategoryService} from '../../core/services/category/category.service';

@Component({
  selector: 'app-category-list-item',
  templateUrl: './category-list-item.component.html',
  styleUrls: ['./category-list-item.component.css']
})
export class CategoryListItemComponent implements OnInit {
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

  onClickCategory(): void {
  }

  getThumbnailUrl(): void {
  }

  onClickDelete(): void{
    this.confirmationService.confirm(
        {
          message: `Are you sure that you want to delete ${this.category?.name} ?
          You will also delete all of its subcategories`,
          acceptLabel: 'Delete',
          rejectLabel: 'Close',
          header: 'Deletion',
          icon: 'pi pi-trash',
          accept: () => this.deletionConfirmed()
        });
  }

  deletionConfirmed(): void {
    this.categoryService.delete(this.category?.id ?? 0).subscribe(() => {
      this.messageService.add({
        severity: 'success',
        summary: 'Deleted successfully',
        detail: 'The category and its subcategories were deleted successfully'
      });
      this.categoryDeleted.emit(this.category.id);
    });
  }

  onClickEdit(): void{
  }

}
