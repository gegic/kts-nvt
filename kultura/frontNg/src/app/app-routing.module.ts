import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {LoginRegisterComponent} from './view/login-registration/login-register.component';
import {EnterEmailComponent} from './view/login-registration/login/enter-email/enter-email.component';
import {EnterPasswordComponent} from './view/login-registration/login/enter-password/enter-password.component';
import {AdminViewComponent} from './view/admin-view/admin-view.component';
import {AuthGuard} from './core/guards/auth/auth.guard';
import {RegisterEmailComponent} from './view/login-registration/registration/register-email/register-email.component';
import {RegisterNameComponent} from './view/login-registration/registration/register-name/register-name.component';

const routes: Routes = [
  { path: '', component: AdminViewComponent, canActivate: [AuthGuard]},
  { path: 'login', component: LoginRegisterComponent, canActivate: [AuthGuard],
    children: [
      { path: '', component: EnterEmailComponent, data: { animation: 'EnterEmail' }},
      { path: 'password', component: EnterPasswordComponent, data: { animation: 'EnterPassword' }}
    ]
  },
  { path: 'register', component: LoginRegisterComponent, canActivate: [AuthGuard],
    children: [
      { path: '', component: RegisterEmailComponent, data: { animation: 'RegisterEmail' }},
      { path: 'name', component: RegisterNameComponent, data: { animation: 'RegisterName' }}
    ]
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
