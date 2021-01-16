import { Component, OnInit } from '@angular/core';
import { Injectable } from '@angular/core';
import { AbstractControl, FormControl, ValidatorFn, Validators } from '@angular/forms';
import { User } from 'src/app/core/models/user';
import { UserService } from 'src/app/core/services/users/users.service';
import {MessageService} from 'primeng/api';
import { AuthService } from 'src/app/core/services/auth/auth.service';

const PASSWORD_REGEX: RegExp = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[a-zA-Z\d]{8,}$/;
const SMALL_REGEX: RegExp = /(?=.*[a-z])/;
const CAPITAL_REGEX: RegExp = /(?=.*[A-Z])/;
const DIGIT_REGEX: RegExp = /(?=.*\d)/;

@Component({
  selector: 'app-user-edit',
  templateUrl: './user-edit.component.html',
  styleUrls: ['./user-edit.component.scss']
})

export class UserEditComponent implements OnInit {


  user: User;

  passwordControl: FormControl = new FormControl('', [this.containDigit(), this.containSmall(), this.containCapital(), this.emptyField('New password')]);
  confirmPasswordControl: FormControl = new FormControl('', [this.samePasswords(this.passwordControl)]);

  name: FormControl = new FormControl('', [this.emptyField('First name')]);
  lastName: FormControl = new FormControl('', [this.emptyField('Last name')]);
  email: FormControl = new FormControl('', [this.emptyField('E-mail')]);

  fields: any[];

  constructor(
      private userService: UserService,
      private messageService: MessageService,
      private authService: AuthService
    ){

    this.user = Object.assign(new User(), this.authService.user.getValue());

    // DEV
    // this.user = new User();
    // this.user.email = 'a@a.com',
    // this.user.firstName = 'Dzoni';
    // this.user.lastName = 'Dep';

    this.fields = [
      {
        name: 'First name',
        value: this.user.firstName
      },
      {
        name: 'Last name',
        value: this.user.lastName
      },
      {
        name: 'E-mail',
        value: this.user.email
      }
    ];
  }

  ngOnInit(): void{
  }

  generateUser(): User{
    const user: User = new User();
    user.id = this.user.id;
    return user;
  }

  updatePassword(): void{
    if (this.passwordControl.invalid){
      console.log(this.passwordControl.errors);
      this.messageService.add({severity: 'error', detail: this.passwordControl.errors?.msg});
      return;
    }
    if (this.confirmPasswordControl.invalid){
      this.messageService.add({severity: 'error', detail: this.confirmPasswordControl.errors?.msg});
      return;
    }
    const user = this.generateUser();
    user.password = this.passwordControl.value;
    this.commitUpdate(user, 'password');
  }
  updateName(): void{
    if (this.name.invalid){
      this.messageService.add({severity: 'error', detail: this.name.errors?.msg});
      return;
    }
    const user = this.generateUser();
    user.firstName = this.name.value;
    this.commitUpdate(user, 'first name');
  }
  updateLastName(): void{
    if (this.lastName.invalid){
      this.messageService.add({severity: 'error', detail: this.lastName.errors?.msg});
      return;
    }
    const user = this.generateUser();
    user.lastName = this.lastName.value;
    this.commitUpdate(user, 'last name');
  }
  updateEmail(): void{
    if (this.email.invalid){
      const errors = this.email.errors;
      if (errors?.email){
        this.messageService.add({severity: 'error', detail: 'E-mail is not valid.'});
        return;
      }
      this.messageService.add({severity: 'error', detail: errors?.msg});
      return;
    }
    const user = this.generateUser();
    user.email = this.email.value;
    this.commitUpdate(user, 'email');
  }

  commitUpdate(user: User, field: string): void{
    this.userService.update(user).subscribe(
      (data) => {
        console.log(data);
        this.userService.getById(user.id).subscribe((data1: {user: User}) => {
          console.log(data1);
          this.authService.updateUserData(user);
        });
        this.messageService.add({severity: 'success', detail: field + ' updated'});
      },
      () => {
        this.messageService.add({severity: 'error', detail: 'Update of ' + field + ' failed'});
      }
    );
  }

  emptyField(field: string): ValidatorFn {
    return (control: AbstractControl): { [key: string]: any } | null =>
      control.value ? null : {msg: field + ' must not be empty.'};
  }

  samePasswords(val: FormControl): ValidatorFn {
    return (control: AbstractControl): { [key: string]: any } | null =>
      control.value === val.value ? null : {msg: 'Passwords must be same.'};
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

