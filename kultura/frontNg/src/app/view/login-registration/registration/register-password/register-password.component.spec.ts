import {ComponentFixture, fakeAsync, TestBed, tick} from '@angular/core/testing';

import { RegisterPasswordComponent } from './register-password.component';
import {RegisterService} from '../../../../core/services/register/register.service';
import {ActivatedRoute, Router} from '@angular/router';
import {of, throwError} from 'rxjs';
import {EnterEmailComponent} from '../../login/enter-email/enter-email.component';
import {RouterTestingModule} from '@angular/router/testing';
import {HttpClientTestingModule} from '@angular/common/http/testing';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {MessageService} from 'primeng/api';
import {RegisterEmailComponent} from '../register-email/register-email.component';
import createSpy = jasmine.createSpy;
import {PasswordModule} from 'primeng/password';

describe('RegisterPasswordComponent', () => {
  let component: RegisterPasswordComponent;
  let fixture: ComponentFixture<RegisterPasswordComponent>;
  let registerService: RegisterService;
  let router: Router;
  let addSpy: any;

  beforeEach(async () => {
    const registerServiceMock = {
      email: 'email@mail.com',
      firstName: 'Ime',
      lastName: 'Prezime',
      password: '',
      register: createSpy('register').and.returnValue(of(null)),
    };
    const routerMock = {
      navigate: createSpy('navigate'),
      navigateByUrl: createSpy('navigateByUrl')
    };
    const activatedRouteMock = {};
    await TestBed.configureTestingModule({
      declarations: [ RegisterPasswordComponent ],
      imports: [HttpClientTestingModule, FormsModule, ReactiveFormsModule, PasswordModule],
      providers: [
        MessageService,
        {provide: RegisterService, useValue: registerServiceMock},
        {provide: Router, useValue: routerMock},
        {provide: ActivatedRoute, useValue: activatedRouteMock}
      ]
    })
      .compileComponents();
  });

  beforeEach(() => {
    registerService = TestBed.inject(RegisterService);
    router = TestBed.inject(Router);
    const messageService = TestBed.inject(MessageService);
    addSpy = spyOn(Object.getPrototypeOf(messageService), 'add');

    fixture = TestBed.createComponent(RegisterPasswordComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should go back to email if there is no email in registrationService', () => {
    registerService.email = '';

    component.ngOnInit();

    expect(router.navigate).toHaveBeenCalledWith(jasmine.arrayContaining(['..']), jasmine.anything());
  });

  it('should go back to email if there is no firstName in registrationService', () => {
    registerService.firstName = '';

    component.ngOnInit();

    expect(router.navigate).toHaveBeenCalledWith(jasmine.arrayContaining(['../name']), jasmine.anything());
  });

  it('should go back to email if there is no lastName in registrationService', () => {
    registerService.lastName = '';

    component.ngOnInit();

    expect(router.navigate).toHaveBeenCalledWith(jasmine.arrayContaining(['../name']), jasmine.anything());
  });

  it('should sign up on valid passwords onSignUp', fakeAsync(() => {
    component.passwordGroup.setValue({password: 'Admin123', repeatPassword: 'Admin123'});

    component.onClickSignUp();

    tick();

    fixture.detectChanges();

    expect(registerService.password).toEqual('Admin123');
    expect(registerService.register).toHaveBeenCalled();
    expect(router.navigate).toHaveBeenCalledWith(jasmine.arrayContaining(['../success']), jasmine.anything());
  }));


  it('should not sign up on invalid both passwords onSignUp', fakeAsync(() => {
    component.passwordGroup.setValue({password: 'admin12', repeatPassword: 'admin12'});

    component.onClickSignUp();

    tick();

    fixture.detectChanges();

    expect(addSpy).toHaveBeenCalled();
  }));

  it('should not sign up on valid but different both passwords onSignUp', fakeAsync(() => {
    component.passwordGroup.setValue({password: 'Admin123', repeatPassword: 'Admin132'});

    component.onClickSignUp();

    tick();

    fixture.detectChanges();

    expect(addSpy).toHaveBeenCalled();
  }));

  it('should not sign up on valid both passwords but api error onSignUp', fakeAsync(() => {
    component.passwordGroup.setValue({password: 'Admin123', repeatPassword: 'Admin123'});

    registerService.register = createSpy('register').and.returnValue(throwError(null));

    component.onClickSignUp();

    tick();

    fixture.detectChanges();

    expect(registerService.password).toEqual('Admin123');
    expect(registerService.register).toHaveBeenCalled();
    expect(addSpy).toHaveBeenCalled();
    expect(router.navigate).toHaveBeenCalledWith(jasmine.arrayContaining(['/register']));

  }));

  it('should navigate back', () => {
    component.onClickBack();

    expect(router.navigate).toHaveBeenCalledWith(jasmine.arrayContaining(['..']), jasmine.anything());
  });

  it('should get both first name and last name', () => {
    registerService.firstName = 'Haris';
    registerService.lastName = 'Gegic';

    expect(component.name).toEqual('Haris Gegic');
  });
});
