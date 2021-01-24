import {Component, OnDestroy, OnInit} from '@angular/core';
import {AbstractControl, FormBuilder, FormGroup, Validators} from '@angular/forms';
import {AuthService} from '../../core/services/auth/auth.service';
import {MenuItem} from 'primeng/api';
import {fader} from './animations';
import {RouterOutlet} from '@angular/router';
import {Title} from '@angular/platform-browser';

@Component({
  selector: 'app-login',
  templateUrl: './login-register.component.html',
  styleUrls: ['./login-register.component.scss'],
  animations: [
    fader
  ]
})
export class LoginRegisterComponent implements OnInit, OnDestroy {

  constructor(private titleService: Title) {
  }

  ngOnInit(): void {
    this.titleService.setTitle('kultura - Log In or Sign up');
  }

  prepareRoute(outlet: RouterOutlet): RouterOutlet {
    return outlet && outlet.activatedRouteData && outlet.activatedRouteData.animation;
  }

  ngOnDestroy(): void {
    this.titleService.setTitle('kultura');
  }
}
