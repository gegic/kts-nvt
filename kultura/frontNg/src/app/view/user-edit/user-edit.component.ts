import {Component, OnInit} from '@angular/core';
import {Injectable} from '@angular/core';
import {AbstractControl, Form, FormControl, FormGroup, ValidationErrors, ValidatorFn, Validators} from '@angular/forms';
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

  user?: User;

  passwordControl: FormControl = new FormControl('', [this.containDigit(), this.containCapital(), this.containSmall(), this.sizeValidator('New password', 8, 50), this.emptyField('New password')]);
  confirmPasswordControl: FormControl = new FormControl('');

  passwordFormControl: FormGroup = new FormGroup({
    password: this.passwordControl,
    confirmPassword: this.confirmPasswordControl
  }, [
    this.samePasswords()
  ]);

  name: FormControl = new FormControl('', [this.emptyField('First name')]);
  lastName: FormControl = new FormControl('', [this.emptyField('Last name')]);
  email: FormControl = new FormControl('', [this.emptyField('E-mail')]);


  constructor(
    private userService: UserService,
    private messageService: MessageService,
    private authService: AuthService
  ) {
  }

  ngOnInit(): void {
    this.user = Object.assign(new User(), this.authService.user.getValue());

  }


  updatePassword(): void {
    if (this.passwordFormControl.invalid) {
      if (this.passwordControl?.invalid) {
        this.messageService.add({severity: 'error',
          summary: 'Not updated', detail: this.passwordControl.errors?.msg, id: 'password-valid-error'});
        return;
      }
      this.messageService.add({severity: 'error',
        summary: 'Not updated', detail: this.passwordFormControl.errors?.msg, id: 'password-error'});
      return;
    }
    const user = this.user;
    if (!user) {
      return;
    }
    user.password = this.passwordControl.value;
    this.commitUpdate(user,
      'password',
      this.passwordControl,
      this.confirmPasswordControl,
      true,
      'Please log in with the new password');
  }

  updateName(): void {
    if (this.name.invalid) {
      this.messageService.add({severity: 'error',
        summary: 'Not updated', detail: this.name.errors?.msg, id: 'name-failed'});
      return;
    }
    const user = this.user;

    if (!user) {
      return;
    }

    user.firstName = this.name.value;
    this.commitUpdate(user, 'First name', this.name);
  }

  updateLastName(): void {
    if (this.lastName.invalid) {
      this.messageService.add({severity: 'error',
        summary: 'Not updated', detail: this.lastName.errors?.msg, id: 'last-name-failed'});
      return;
    }
    const user = this.user;

    if (!user) {
      return;
    }

    user.lastName = this.lastName.value;
    this.commitUpdate(user, 'Last name', this.lastName);
  }

  updateEmail(): void {
    if (this.email.invalid) {
      const errors = this.email.errors;
      if (errors?.email) {
        this.messageService.add({severity: 'error',
          summary: 'Not updated', detail: 'E-mail is not valid', id: 'email-valid-failed'});
        return;
      }
      this.messageService.add({severity: 'error',
        summary: 'Not updated', detail: errors?.msg, id: 'email-failed'});
      return;
    }
    const user = this.user;

    if (!user) {
      return;
    }

    user.email = this.email.value;
    this.commitUpdate(user,
      'email',
      this.email,
      null,
      true,
      'Please verify new e-mail address.');
  }

  commitUpdate(user: User,
               field: string,
               formControl: FormControl,
               optionalControl: FormControl | null = null,
               signOutAfter: boolean = false,
               signOutMessage: string = ''): void {
    this.userService.update(user).subscribe(
      val => {
        this.user = val;
        this.authService.updateUserData(val);
        this.messageService.add({severity: 'success', summary: 'Success',
          detail: field + ' updated', id: 'update-successful'});
        if (signOutAfter) {
          this.authService.logout();
          this.messageService.add({severity: 'info',
            summary: 'Logged out', detail: signOutMessage, id: 'sign-out-toast'});
        }
      },
      () => {
        this.messageService.add({severity: 'error', detail: 'Update of ' + field + ' failed'});
      },
      () => {
        formControl.reset();
        if (!!optionalControl) {
          optionalControl.reset();
        }
      }
    );
  }

  sizeValidator(field: string, min: number, max: number): ValidatorFn {
    return (control: AbstractControl): { [key: string]: any } | null =>
      control.value?.length >= min && control.value?.length <= max ? null : {msg: field + ' length must be between ' + min + ' and ' + max + ' characters.'};
  }

  emptyField(field: string): ValidatorFn {
    return (control: AbstractControl): { [key: string]: any } | null =>
      control.value ? null : {msg: field + ' must not be empty.'};
  }

  samePasswords(): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
      const password = control.get('password');
      const confirmPassword = control.get('confirmPassword');
      return password?.value === confirmPassword?.value ? null : {msg: 'Passwords must be same.'};
    };
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

  getFields(): any[] {

    if (!this.user) {
      return [];
    }

    return [
      {
        id: 'name-val',
        name: 'First name',
        value: this.user.firstName
      },
      {
        id: 'lastName-val',
        name: 'Last name',
        value: this.user.lastName
      },
      {
        id: 'email-val',
        name: 'E-mail',
        value: this.user.email
      }
    ];
  }
}

