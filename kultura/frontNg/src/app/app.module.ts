import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { AppComponent } from './app.component';
import { AdminViewComponent } from './view/admin-view/admin-view.component';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import { AppRoutingModule } from './app-routing.module';
import { LoginRegisterComponent } from './view/login-registration/login-register.component';
import {InputTextModule} from 'primeng/inputtext';
import {CardModule} from 'primeng/card';
import {StepsModule} from 'primeng/steps';
import {PasswordModule} from 'primeng/password';
import {ButtonModule} from 'primeng/button';
import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';
import { EnterEmailComponent} from './view/login-registration/login/enter-email/enter-email.component';
import { EnterPasswordComponent } from './view/login-registration/login/enter-password/enter-password.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import {ToastModule} from 'primeng/toast';
import {MessageService} from 'primeng/api';
import {JwtInterceptor} from './core/interceptors/jwt.interceptor';
import { RegisterEmailComponent } from './view/login-registration/registration/register-email/register-email.component';
import { RegisterNameComponent } from './view/login-registration/registration/register-name/register-name.component';
import { RegisterPasswordComponent } from './view/login-registration/registration/register-password/register-password.component';
import { RegisterSuccessComponent } from './view/login-registration/registration/register-success/register-success.component';

@NgModule({
  declarations: [
    AppComponent,
    AdminViewComponent,
    LoginRegisterComponent,
    EnterEmailComponent,
    EnterPasswordComponent,
    RegisterEmailComponent,
    RegisterNameComponent,
    RegisterPasswordComponent,
    RegisterSuccessComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    AppRoutingModule,
    InputTextModule,
    ReactiveFormsModule,
    StepsModule,
    CardModule,
    PasswordModule,
    ButtonModule,
    HttpClientModule,
    BrowserAnimationsModule,
    ToastModule,
  ],
  providers: [
    { provide: HTTP_INTERCEPTORS, useClass: JwtInterceptor, multi: true },
    MessageService
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
