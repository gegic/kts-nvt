import { Component, OnInit } from '@angular/core';
import {FormControl, Validators} from '@angular/forms';
import {LoginService} from '../../../../core/services/login/login.service';
import {ActivatedRoute, Router} from '@angular/router';
import {MessageService} from 'primeng/api';
import {RegisterService} from '../../../../core/services/register/register.service';

@Component({
  selector: 'app-register-email',
  templateUrl: './register-email.component.html',
  styleUrls: ['./register-email.component.scss']
})
export class RegisterEmailComponent implements OnInit {

  emailControl: FormControl;

  constructor(private registerService: RegisterService,
              private router: Router,
              private messageService: MessageService,
              private activatedRoute: ActivatedRoute) {
    this.emailControl = new FormControl('', [Validators.required, Validators.email]);
  }

  ngOnInit(): void {
  }

  onClickProceed(): void {
    if (!this.emailControl.valid) {
      this.messageService.add({severity: 'error', summary: 'Email not valid', detail: 'Email should be formatted properly.'});
      return;
    }

    const email = this.emailControl.value;

    this.registerService.checkExistence(email)
      .subscribe(
        data => {
          this.messageService.add({severity: 'error', summary: 'Email already exists',
            detail: 'An account with this email already exists.'});
          this.emailControl.reset();
        },
        error => {
          this.registerService.email = email;
          this.router.navigate(['./name'], {relativeTo: this.activatedRoute});
        }
      );
  }

  onClickSignIn(): void {
    this.registerService.reset();
    this.router.navigateByUrl('/login');
  }

}
