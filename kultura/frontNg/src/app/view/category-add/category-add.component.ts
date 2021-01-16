import { Component, OnInit } from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {Moderator} from '../../core/models/moderator';
import {RegisterService} from '../../core/services/register/register.service';
import {ModeratorService} from '../../core/services/moderator/moderator.service';
import {MessageService} from 'primeng/api';
import {Router} from '@angular/router';
import {CategoryService} from '../../core/services/category/category.service';
import {Category} from '../../core/models/category';

@Component({
  selector: 'app-category-add',
  templateUrl: './category-add.component.html',
  styleUrls: ['../moderator-add/moderator-add.component.css']
})
export class CategoryAddComponent implements OnInit {
  categoryForm: FormGroup = new FormGroup(
    {
      categoryName: new FormControl(undefined, [Validators.required, Validators.pattern(/[\p{L} \d]+/u)])
    }
  );

  constructor(private registerService: RegisterService,
              private categoryService: CategoryService,
              private messageService: MessageService,
              private router: Router) {
  }


  ngOnInit(): void {
  }

  onSubmit(): void {
    console.log('ADD CATEGORY');
    console.log(this.categoryForm.controls);
    if (this.categoryForm.controls.categoryName.invalid) {
      this.messageService.add({severity: 'error', detail: 'Category name cannot be empty and must start with a capital letter'});
      this.categoryForm.patchValue({categoryName: ''});
    }
    const category: Category = {
      name : this.categoryForm.controls.categoryName.value,
    };
    if (this.categoryForm.invalid) {
      return;
    }
    const exists = this.categoryService.checkExists(this.categoryForm.controls.categoryName.value);
    if (exists){
      this.messageService.add({
        severity: 'error', summary: 'Category name already exists',
        detail: 'An category with this name already exists.'
      });
      this.categoryForm.patchValue({name: ''});
    }
    else{
      this.categoryService.createCategory(category);
      this.messageService.add({
        severity: 'success', summary: 'Category added successfully.',
        detail: 'You can now modify the category and add subcategories to it.'
      });
      this.router.navigate(['admin-panel/categories']);
    }
  }
}
