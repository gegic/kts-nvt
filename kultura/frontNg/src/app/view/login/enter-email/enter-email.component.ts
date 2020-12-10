import { Component, OnInit } from '@angular/core';
import {LoginService} from '../../../core/services/login/login.service';
import {FormBuilder, FormControl, FormGroup} from '@angular/forms';
import {ActivatedRoute, Route, Router} from '@angular/router';
import {MessageService} from 'primeng/api';
import {User} from '../../../core/models/user';

@Component({
  selector: 'app-enter-email',
  templateUrl: './enter-email.component.html',
  styleUrls: ['./enter-email.component.scss']
})
export class EnterEmailComponent implements OnInit {

  emailForm: FormControl;

  constructor(private loginService: LoginService,
              private router: Router,
              private messageService: MessageService,
              private activatedRoute: ActivatedRoute) {
    this.emailForm = new FormControl();
  }

  ngOnInit(): void {
  }

  onClickProceed(): void {
    this.loginService.checkExistence(this.emailForm.value)
      .subscribe(
        (data: {value: string}) => {
          this.loginService.email = this.emailForm.value;
          this.loginService.name = data.value;
          this.router.navigate(['./password'], {relativeTo: this.activatedRoute});
        },
        err => {
          this.emailForm.reset();
          this.messageService.add({severity: 'error', summary: 'Email not found', detail: 'A user with this email doesn\'t exist.'});
        }
      );
  }

}
