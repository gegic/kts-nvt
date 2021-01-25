import {ComponentFixture, fakeAsync, TestBed, tick} from '@angular/core/testing';

import { EnterPasswordComponent } from './enter-password.component';
import {LoginService} from '../../../../core/services/login/login.service';
import {ActivatedRoute, Router} from '@angular/router';
import {Observable, of, throwError} from 'rxjs';
import {EnterEmailComponent} from '../enter-email/enter-email.component';
import {RouterTestingModule} from '@angular/router/testing';
import {HttpClientTestingModule} from '@angular/common/http/testing';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {MessageService} from 'primeng/api';
import createSpy = jasmine.createSpy;
import {AuthService} from '../../../../core/services/auth/auth.service';
import {Test} from 'tslint';
import {PasswordModule} from 'primeng/password';
import {User} from '../../../../core/models/user';
import {log} from 'util';

describe('EnterPasswordComponent', () => {
  let component: EnterPasswordComponent;
  let fixture: ComponentFixture<EnterPasswordComponent>;
  let loginService: LoginService;
  let authService: AuthService;
  let router: Router;
  let addSpy: any;

  beforeEach(async () => {
    const user = new User();
    user.id = 1;
    user.email = 'somemail@mail.com';
    user.verified = true;
    user.firstName = 'Ime';
    user.lastName = 'Prezime';
    const loginServiceMock = {
      email: '',
      name: '',
      login: createSpy('login').and.returnValue(of({token: 'sometoken', user})),
      reset: createSpy('reset')
    };
    const routerMock = {
      navigate: createSpy('navigate'),
      navigateByUrl: createSpy('navigateByUrl')
    };
    const authServiceMock = {
      login: createSpy('login')
    };
    const activatedRouteMock = {};
    await TestBed.configureTestingModule({
      declarations: [ EnterPasswordComponent ],
      imports: [HttpClientTestingModule, FormsModule, ReactiveFormsModule, PasswordModule],
      providers: [
        MessageService,
        {provide: LoginService, useValue: loginServiceMock},
        {provide: Router, useValue: routerMock},
        {provide: ActivatedRoute, useValue: activatedRouteMock},
        {provide: AuthService, useValue: authServiceMock}
      ]
    })
      .compileComponents();
  });

  beforeEach(() => {
    loginService = TestBed.inject(LoginService);
    loginService.email = 'somemail@mail.com';

    authService = TestBed.inject(AuthService);
    router = TestBed.inject(Router);
    const messageService = TestBed.inject(MessageService);
    addSpy = spyOn(Object.getPrototypeOf(messageService), 'add');

    fixture = TestBed.createComponent(EnterPasswordComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });


  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should redirect to previous page if there is no email specified in loginservice', () => {
    loginService.email = '';

    component.ngOnInit();

    expect(router.navigate).toHaveBeenCalledWith(jasmine.arrayContaining(['..']), jasmine.anything());
  });

  it('should not redirect to previous page if there is email specified', () => {

    component.ngOnInit();

    expect(router.navigate).not.toHaveBeenCalled();
  });

  it('should login on onClickLogin()', fakeAsync(() => {
    component.passwordControl.setValue('admin123');

    component.onClickLogin();

    tick();

    fixture.detectChanges();

    expect(loginService.password).toEqual('admin123');
    expect(component.loading).toBeFalse();
    expect(authService.login).toHaveBeenCalledWith('sometoken', jasmine.any(User));
    expect(router.navigateByUrl).toHaveBeenCalledWith('/');
  }));

  it('should not login on onClickLogin()', fakeAsync(() => {
    const passwordResetSpy = spyOn(component.passwordControl, 'reset');
    loginService.login = createSpy('login').and.returnValue(throwError(null));
    component.passwordControl.setValue('admin123');

    component.onClickLogin();

    tick();

    fixture.detectChanges();

    expect(loginService.password).toEqual('admin123');
    expect(component.loading).toBeFalse();
    expect(passwordResetSpy).toHaveBeenCalled();
  }));

  it('should not accept invalid password', fakeAsync(() => {
    const passwordResetSpy = spyOn(component.passwordControl, 'reset');
    loginService.login = createSpy('login').and.returnValue(throwError(null));
    component.passwordControl.setValue('123');

    component.onClickLogin();

    tick();

    fixture.detectChanges();

    expect(addSpy).toHaveBeenCalled();
  }));

  it('should switch navigate to the previous page', () => {
    component.onSwitchAccount();

    expect(loginService.reset).toHaveBeenCalled();
    expect(router.navigate).toHaveBeenCalledWith(jasmine.arrayContaining(['..']), jasmine.anything());
  });

  it('should retrieve name from loginService', () => {
    loginService.name = 'NAME';

    expect(component.name).toEqual('NAME');
  });

  it('should unsubscribe on destroy', fakeAsync(() => {
    component.onClickLogin();

    tick();
    fixture.detectChanges();

    component.ngOnDestroy();
    expect((component as any).subscription.closed).toBeTrue();
  }));
});
