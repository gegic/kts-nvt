import {Component, Input, OnInit} from '@angular/core';
import {CategoryService} from '../../core/services/category/category.service';
import {SubcategoryService} from '../../core/services/subcategories/subcategory.service';
import {Category} from '../../core/models/category';
import {ActivatedRoute} from '@angular/router';

@Component({
  selector: 'app-subcategories-view',
  templateUrl: './subcategories-view.component.html',
  styleUrls: ['./subcategories-view.component.css']
})
export class SubcategoriesViewComponent implements OnInit {
  @Input()
  category!: Category | undefined;
  subcategoriesList: any[] = [];

  constructor(private categoryService: CategoryService,
              private subcategoryService: SubcategoryService,
              private activatedRoute: ActivatedRoute) { }

  ngOnInit(): void {
    this.activatedRoute.params.subscribe(
      val => {
        this.subcategoryService.getSubcategories(val.id).subscribe(subcategories => {
            console.log('subcat content ' + subcategories.content);
            this.subcategoriesList = subcategories.content || [];
            console.log(this.subcategoriesList);
          }
        );
      }
      );

    // // @ts-ignore
    // this.subcategoryService.getSubcategories(this.category.id).subscribe(subcategories => {
    //   console.log('subcat content ' + subcategories.content);
    //   this.subcategoriesList = subcategories.content || [];
    //   console.log(this.subcategoriesList);
    // });
  }

  deleteSubcategory($event: any): void {
  }

  onScrollDown(): void {

  }
}
