import { Component, OnInit } from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {MessageService} from 'primeng/api';
import {CategoryService} from '../../core/services/category/category.service';
import {Moderator} from '../../core/models/moderator';
import {Category} from '../../core/models/category';
import {FormControl, FormGroup, Validators} from '@angular/forms';

@Component({
  selector: 'app-category-edit',
  templateUrl: './category-edit.component.html',
  styleUrls: ['./category-edit.component.css']
})
export class CategoryEditComponent implements OnInit {
  oldCategory: Category | undefined;
  categoryForm: FormGroup = new FormGroup(
    {
      id : new FormControl(undefined),
      name: new FormControl(undefined, [Validators.required, Validators.pattern(/[\p{L} \d]+/u)])
    }
  );
  constructor(private activatedRoute: ActivatedRoute,
              private messageService: MessageService,
              private categoryService: CategoryService,
              private router: Router) { }

  ngOnInit(): void {
    this.activatedRoute.params.subscribe(
      val => {
        this.categoryService.getCategory(val.id).subscribe(
          data => {
            console.log(data);
            this.categoryForm.patchValue({
              name: data.name,
              id: val.id
            });
            this.oldCategory = data;
          }
        );
        console.log(val);
      }
    );
  }

  onSubmit(): void{

  }
}
