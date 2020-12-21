import { Component, OnInit } from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';

@Component({
  selector: 'app-moderator-add',
  templateUrl: './moderator-add.component.html',
  styleUrls: ['./moderator-add.component.css']
})
export class ModeratorAddComponent implements OnInit {
  formGroup: FormGroup = new FormGroup(
    {
      firstName: new FormControl(undefined, [Validators.required, Validators.pattern(/[\p{L} \d]+/u)]),
      lastName: new FormControl(undefined, [Validators.required, Validators.pattern(/[\p{L} \d]+/u)]),
      password : new FormControl(undefined, [Validators.required,  Validators.pattern('(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[$@$!%*?&])[A-Za-z\d$@$!%*?&].{8,}')]),
      repeatPassword : new FormControl(undefined, [Validators.required,  Validators.pattern('(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[$@$!%*?&])[A-Za-z\d$@$!%*?&].{8,}')]),
      email : new FormControl(undefined, [Validators.required, Validators.pattern('^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}$')]),
    }
  );
  constructor() { }

  ngOnInit(): void {
  }

  onSubmit(): void {
    console.log('ADD MODERATOR');
    console.log(this.formGroup.controls);
  }

}
