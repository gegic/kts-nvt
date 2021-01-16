import { Component, OnInit } from '@angular/core';
import {ModeratorService} from '../../core/services/moderator/moderator.service';
import {CategoryService} from '../../core/services/category/category.service';

@Component({
  selector: 'app-categories-view',
  templateUrl: './categories-view.component.html',
  styleUrls: ['./categories-view.component.css']
})
export class CategoriesViewComponent implements OnInit {

  categoriesList: any[] = [];
  page = 0;
  totalPages = 1;

  constructor(private categoryService: CategoryService) {
  }

  ngOnInit(): void {
    this.categoryService.getCategories(0).subscribe(categories => {
      this.categoriesList = categories.content || [];
    });
  }

  deleteCategory(id: string): void{
    let result: any[] = [];
    result = this.categoriesList.filter(x => x.id !== id);
    this.categoriesList = result || [];
  }

  onScrollDown(): void{
    this.getCategories();
  }

  getCategories(): void{
    if (this.page === this.totalPages) {
      return;
    }
    this.categoryService.getCategories(this.page + 1).subscribe(
      val => {
        for (const el of val.content) {
          if (this.categoryService.categoriesList.some(category => category.id === el.id)) {
            continue;
          }
          this.categoriesList.push(el);
        }
        this.page = val.pageable.pageNumber;
        this.totalPages = val.totalPages;
      }
    );
  }
}
