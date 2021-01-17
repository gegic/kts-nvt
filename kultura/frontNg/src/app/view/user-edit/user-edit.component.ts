import {Component, OnInit} from '@angular/core';
import {Injectable} from '@angular/core';
import {AbstractControl, FormControl, ValidatorFn, Validators} from '@angular/forms';
import {User} from 'src/app/core/models/user';
import {UserService} from 'src/app/core/services/users/users.service';
import {MessageService} from 'primeng/api';
import {AuthService} from 'src/app/core/services/auth/auth.service';

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

  name: FormControl = new FormControl('', [this.emptyField('First name'), this.sameAsOld('First name', this.user?.firstName)]);
  lastName: FormControl = new FormControl('', [this.emptyField('Last name'), this.sameAsOld('First name', this.user?.lastName)]);
  email: FormControl = new FormControl('', [this.emptyField('E-mail'), this.sameAsOld('First name', this.user?.email)]);

  fields: any[] = [];

  constructor(
    private userService: UserService,
    private messageService: MessageService,
    private authService: AuthService
  ) {

    this.user = Object.assign(new User(), this.authService.user.getValue());
    this.refreshUserData(this.user);
  }

  ngOnInit(): void {
  }

  generateUser(): User {
    const user: User = new User();
    user.authorities = null;
    user.firstName = null;
    user.lastName = null;
    user.email = null;
    user.verified = null;
    user.password = null;
    user.lastPasswordChange = null;
    user.id = this.user.id;
    return user;
  }

  updatePassword(): void {
    if (this.passwordControl.invalid) {
      console.log(this.passwordControl.errors);
      this.messageService.add({severity: 'error', detail: this.passwordControl.errors?.msg});
      return;
    }
    if (this.confirmPasswordControl.invalid) {
      this.messageService.add({severity: 'error', detail: this.confirmPasswordControl.errors?.msg});
      return;
    }
    const user = this.generateUser();
    user.password = this.passwordControl.value;
    this.commitUpdate(user, 'password')
      .then(() => {
        this.messageService.add({severity: 'info', detail: 'Log in using new password.'});
        this.passwordControl.reset();
        this.confirmPasswordControl.reset();
        this.authService.logout();
      });
  }

  updateName(): void {
    if (this.name.invalid) {
      this.messageService.add({severity: 'error', detail: this.name.errors?.msg});
      return;
    }
    const user = this.generateUser();
    user.firstName = this.name.value;
    this.commitUpdate(user, 'first name').then(() => {
      this.name.reset();
    });
  }

  updateLastName(): void {
    if (this.lastName.invalid) {
      this.messageService.add({severity: 'error', detail: this.lastName.errors?.msg});
      return;
    }
    const user = this.generateUser();
    user.lastName = this.lastName.value;
    this.commitUpdate(user, 'last name').then(() => {
      this.lastName.reset();
    });
  }

  updateEmail(): void {
    if (this.email.invalid) {
      const errors = this.email.errors;
      if (errors?.email) {
        this.messageService.add({severity: 'error', detail: 'E-mail is not valid.'});
        return;
      }
      this.messageService.add({severity: 'error', detail: errors?.msg});
      return;
    }
    const user = this.generateUser();
    user.email = this.email.value;
    this.commitUpdate(user, 'email')
      .then(() => {
        this.messageService.add({severity: 'info', detail: 'Please verify new email address.'});
        this.email.reset();
        this.authService.logout();
      });
  }

  commitUpdate(user: User, field: string): Promise<unknown> {
    return new Promise((resolve, reject) => {
      this.userService.update(user).subscribe(
        (data) => {
          console.log(data);
          this.authService.updateUserData(data);
          this.refreshUserData(data as User);
          this.messageService.add({severity: 'success', detail: field + ' has been updated'});
          resolve();
        },
        () => {
          this.messageService.add({severity: 'error', detail: 'Update of ' + field + ' failed'});
          reject();
        }
      );
    });
  }

  refreshUserData(user: User): void {
    this.user = user;
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

  sameAsOld(field: string, value: any): ValidatorFn {
    return (control: AbstractControl): { [key: string]: any } | null =>
      control.value === value ? {msg: field + ' must not be same as old.'} : null;
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

