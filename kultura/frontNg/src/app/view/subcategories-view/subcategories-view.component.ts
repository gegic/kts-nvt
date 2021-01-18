import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {DynamicDialogConfig, DynamicDialogRef} from 'primeng/dynamicdialog';
import {Category} from '../../core/models/category';
import {CategoryService} from '../../core/services/category/category.service';
import {Subcategory} from '../../core/models/subcategory';
import {FormControl, Validators} from '@angular/forms';
import {LazyLoadEvent} from 'primeng/api';

@Component({
  selector: 'app-subcategories-view',
  templateUrl: './subcategories-view.component.html',
  styleUrls: ['./subcategories-view.component.scss']
})
export class SubcategoriesViewComponent implements OnInit {

  @ViewChild('dt')
  table!: ElementRef;

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
              private categoryService: CategoryService) { }

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
  }

  deleteSubcategory(subcategory: Subcategory): void {
  }

  onHideSubcategoryDialog(): void {
  }

  saveSubcategory(): void {
  }

  get editing(): boolean {
    return !!this.editingSubcategory;
  }
}
