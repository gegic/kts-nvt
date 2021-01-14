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

  constructor(private categoryService: CategoryService) {
  }

  ngOnInit(): void {
    this.categoryService.getCategories().subscribe(categories => {
      this.categoriesList = categories.content || [];
    });
  }

  onClickLogout(): void {
    console.log(this.categoryService.getCategories());
  }

  deleteCategory(id: string): void{
    let result: any[] = [];
    result = this.categoriesList.filter(x => x.id !== id);
    this.categoriesList = result || [];
  }

}
