import { Component, OnInit } from '@angular/core';
import { Injectable } from '@angular/core';
import { AbstractControl, FormControl, ValidatorFn, Validators } from '@angular/forms';
import { User } from 'src/app/core/models/user';
import { UserService } from 'src/app/core/services/users/users.service';
import {MessageService} from 'primeng/api';

const PASSWORD_REGEX: RegExp = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[a-zA-Z\d]{8,}$/;
const SMALL_REGEX: RegExp = /(?=.*[a-z])/;
const CAPITAL_REGEX: RegExp = /(?=.*[A-Z])/;
const DIGIT_REGEX: RegExp = /(?=.*\d)/;

@Component({
  selector: 'app-user-edit',
  templateUrl: './user-edit.component.html',
  styleUrls: ['./user-edit.component.css']
})

export class UserEditComponent implements OnInit {


  user: User;
  passwordControl: FormControl = new FormControl('', [this.containDigit(), this.containSmall(), this.containCapital()]);
  confirmPasswordControl: FormControl = new FormControl('', [this.samePasswords(this.passwordControl)]);

  name = '';
  lastName = '';
  email: FormControl = new FormControl('');

  constructor(
      private userService: UserService,
      private messageService: MessageService
    ){
    // this.user = JSON.parse(localStorage.getItem("user"));
    this.user = new User();
    this.user.email = 'a@a.com',
    this.user.firstName = 'Dzoni';
    this.user.lastName = 'Dep';
  }

  ngOnInit(): void{
  }

  updateInfo(): void{
    if (this.email.value && this.email.invalid){
      this.messageService.add({severity: 'error', detail: 'E-mail is not valid.'});
      return;
    }
    const user: User = new User();
    user.id = this.user.id;
    user.email = this.email.value;
    user.firstName = this.name;
    user.lastName = this.lastName;
    this.userService.update(user).subscribe(
      (data: {user: User}) => {
        console.log(data);
      },
      () => {
        this.messageService.add({severity: 'error', detail: 'Update failed'});
      }
    );
  }
  updatePassword(): void{
    if (this.passwordControl.invalid){
      console.log(this.passwordControl.errors);
      this.messageService.add({severity: 'error', detail: 'Password is not valid.'});
      return;
    }
    if (this.confirmPasswordControl.invalid){
      this.messageService.add({severity: 'error', detail: this.confirmPasswordControl.errors?.msg});
      return;
    }

    const user: User = new User();
    user.id = this.user.id;
    user.password = this.passwordControl.value;
    this.userService.update(user).subscribe(
      (data: {user: User}) => {
        console.log(data);
      },
      () => {
        this.messageService.add({severity: 'error', detail: 'Update failed'});
      });
  }

  samePasswords(val: FormControl): ValidatorFn {
    return (control: AbstractControl): { [key: string]: any } | null =>
      control.value === val.value ? null : {msg: 'Passwords must be same'};
  }

  containCapital(): ValidatorFn {
    return (control: AbstractControl): { [key: string]: any } | null =>
    CAPITAL_REGEX.test(control.value) ? null : {msg: 'Password must contain capital letter.'};
  }

  containSmall(): ValidatorFn {
    return (control: AbstractControl): { [key: string]: any } | null =>
    SMALL_REGEX.test(control.value) ? null : {msg: 'Password must contain letter.'};
  }

  containDigit(): ValidatorFn {
    return (control: AbstractControl): { [key: string]: any } | null =>
    DIGIT_REGEX.test(control.value) ? null : {msg: 'Password must contain digit.'};
  }
}

