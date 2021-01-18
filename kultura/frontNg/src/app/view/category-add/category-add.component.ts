import { Component, OnInit } from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';

@Component({
  selector: 'app-category-add',
  templateUrl: './category-add.component.html',
  styleUrls: ['../moderator-add/moderator-add.component.scss']
})
export class CategoryAddComponent implements OnInit {
  categoryForm: FormGroup = new FormGroup(
    {
      firstName: new FormControl(undefined, [Validators.required, Validators.pattern(/[\p{L} \d]+/u)]),
      lastName: new FormControl(undefined, [Validators.required, Validators.pattern(/[\p{L} \d]+/u)]),
    }
  );

  constructor() { }

  ngOnInit(): void {
  }

  onSubmit(): void {
  }

}
