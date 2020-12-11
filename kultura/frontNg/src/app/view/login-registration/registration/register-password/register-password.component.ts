import { Component, OnInit } from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {RegisterService} from '../../../../core/services/register/register.service';
import {ActivatedRoute, Router} from '@angular/router';
import {MessageService} from 'primeng/api';

const PASSWORD_REGEX: RegExp = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[a-zA-Z\d]{8,}$/;

@Component({
  selector: 'app-register-password',
  templateUrl: './register-password.component.html',
  styleUrls: ['./register-password.component.scss']
})
export class RegisterPasswordComponent implements OnInit {

  passwordGroup: FormGroup;

  constructor(private registerService: RegisterService,
              private router: Router,
              private activatedRoute: ActivatedRoute,
              private messageService: MessageService) {
    this.passwordGroup = new FormGroup({
      password: new FormControl('', [Validators.pattern(PASSWORD_REGEX)]),
      repeatPassword: new FormControl('', Validators.pattern(PASSWORD_REGEX))
    });
  }

  ngOnInit(): void {
    if (!this.registerService.email) {
      this.router.navigate(['..'], {relativeTo: this.activatedRoute});
    } else if (!this.registerService.firstName || !this.registerService.lastName) {
      this.router.navigate(['../name'], {relativeTo: this.activatedRoute});
    }
  }

  onClickSignUp(): void {
    if (this.passwordGroup.invalid) {
      this.messageService.add({severity: 'error',
        detail: 'Password has to contain at least one uppercase, one lowercase letter and one digit. ' +
          'It has to be at least 8 characters long'});
      return;
    }
    const password: string = this.passwordGroup.get('password')?.value;
    const repeatPassword: string = this.passwordGroup.get('repeatPassword')?.value;
    if (password !== repeatPassword) {
      this.messageService.add({severity: 'error',
        detail: 'Repeated password has to match the original password.'});
      return;
    }

    this.registerService.register()
      .subscribe(
        () => {
          this.router.navigate(['../success'], {relativeTo: this.activatedRoute});
        },
        () => {
          this.messageService.add({severity: 'error', summary: 'Unexpected error', detail: 'An unexpected error occurred.'});
          this.router.navigate(['/register']);
        }
      );

  }

  onClickBack(): void {
    this.router.navigate(['..'], {relativeTo: this.activatedRoute});
  }

  get name(): string {
    return `${this.registerService.firstName} ${this.registerService.lastName}`;
  }

}
