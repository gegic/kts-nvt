import { Component, OnInit } from '@angular/core';
import {FormControl, Validators} from '@angular/forms';
import {LoginService} from '../../../core/services/login/login.service';
import {ActivatedRoute, Router} from '@angular/router';
import {relativeFrom} from '@angular/compiler-cli/src/ngtsc/file_system';
import {AuthService} from '../../../core/services/auth/auth.service';
import {MessageService} from 'primeng/api';
import {User} from '../../../core/models/user';
import {tokenize} from '@angular/compiler/src/ml_parser/lexer';
import {useAnimation} from '@angular/animations';

@Component({
  selector: 'app-enter-password',
  templateUrl: './enter-password.component.html',
  styleUrls: ['./enter-password.component.scss']
})
export class EnterPasswordComponent implements OnInit {

  passwordForm: FormControl;

  constructor(private loginService: LoginService,
              private router: Router,
              private activatedRoute: ActivatedRoute,
              private authService: AuthService,
              private messageService: MessageService) {
    this.passwordForm = new FormControl(null, [Validators.minLength(8)]);
  }

  ngOnInit(): void {
  }

  onClickLogin(): void {
    if (!this.passwordForm.valid) {
      this.messageService.add({severity: 'error', detail: 'Passwords are at least eight characters long.'});
      return;
    }
    this.loginService.password = this.passwordForm.value;
    this.loginService.login()
      .subscribe(
        (data: {token: string, user: User}) => {
          this.authService.login(data.token, data.user);
          this.router.navigateByUrl('/');
        }
      );
  }

  onSwitchAccount(): void {
    this.loginService.reset();
    this.router.navigate(['..'], {relativeTo: this.activatedRoute});
  }

  get name(): string {
    return this.loginService.name;
  }
}
