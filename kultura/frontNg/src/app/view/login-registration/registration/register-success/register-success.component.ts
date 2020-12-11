import { Component, OnInit } from '@angular/core';
import {RegisterService} from '../../../../core/services/register/register.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-register-success',
  templateUrl: './register-success.component.html',
  styleUrls: ['./register-success.component.scss']
})
export class RegisterSuccessComponent implements OnInit {

  constructor(private registerService: RegisterService,
              private router: Router) { }

  ngOnInit(): void {
    if (!this.registerService.email ||
        !this.registerService.firstName ||
        !this.registerService.lastName ||
        !this.registerService.password) {
      console.log(this.registerService);
      this.router.navigate(['/login']);
    }
  }

  onClickSignIn(): void {
    this.router.navigate(['/login']);
  }

  get name(): string {
    return `${this.registerService.firstName} ${this.registerService.lastName}`;
  }

}
