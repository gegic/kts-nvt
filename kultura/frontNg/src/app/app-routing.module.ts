import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {LoginRegisterComponent} from './view/login/login-register.component';
import {EnterEmailComponent} from './view/login/enter-email/enter-email.component';
import {EnterPasswordComponent} from './view/login/enter-password/enter-password.component';
import {EnterPasswordGuard} from './core/guards/enter-password/enter-password.guard';
import {AdminViewComponent} from './view/admin-view/admin-view.component';
import {AuthGuard} from './core/guards/auth/auth.guard';

const routes: Routes = [
  { path: '', component: AdminViewComponent, canActivate: [AuthGuard]},
  { path: 'login', component: LoginRegisterComponent, canActivate: [AuthGuard],
    children: [
      { path: '', component: EnterEmailComponent, data: { animation: 'EnterEmail' }},
      { path: 'password', component: EnterPasswordComponent, data: { animation: 'EnterPassword' }, canActivate: [EnterPasswordGuard]}
    ]
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
