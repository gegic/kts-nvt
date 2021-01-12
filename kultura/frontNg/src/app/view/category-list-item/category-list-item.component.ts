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
  }

  onClickEdit(): void{
  }

}
