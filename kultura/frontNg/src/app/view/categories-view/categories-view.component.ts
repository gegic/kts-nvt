import { Component, OnInit } from '@angular/core';
import {ModeratorService} from '../../core/services/moderator/moderator.service';
import {CategoryService} from '../../core/services/category/category.service';

@Component({
  selector: 'app-categories-view',
  templateUrl: './categories-view.component.html',
  styleUrls: ['./categories-view.component.css']
})
export class CategoriesViewComponent implements OnInit {

  categoriesList: [] | undefined = [];

  constructor(private categoryService: CategoryService) {
  }

  ngOnInit(): void {
    // tslint:disable-next-line:no-debugger
    debugger;
    this.categoryService.getCategories().subscribe(categories => {
      this.categoriesList = categories.content;
    });
  }

  onClickLogout(): void {
    console.log(this.categoryService.getCategories());
  }
}
