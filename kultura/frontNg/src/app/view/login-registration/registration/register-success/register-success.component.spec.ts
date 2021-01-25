import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RegisterSuccessComponent } from './register-success.component';
import {RegisterService} from '../../../../core/services/register/register.service';
import {ActivatedRoute, Router} from '@angular/router';
import {of} from 'rxjs';
import createSpy = jasmine.createSpy;
import {EnterEmailComponent} from '../../login/enter-email/enter-email.component';
import {RouterTestingModule} from '@angular/router/testing';
import {HttpClientTestingModule} from '@angular/common/http/testing';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {MessageService} from 'primeng/api';

describe('RegisterSuccessComponent', () => {
  let component: RegisterSuccessComponent;
  let fixture: ComponentFixture<RegisterSuccessComponent>;
  let registerService: RegisterService;
  let router: Router;

  beforeEach(async () => {
    const registerServiceMock = {
      email: 'email@mail.com',
      firstName: 'Ime',
      lastName: 'Prezime',
      password: 'Admin123',
      checkExistence: createSpy('checkExistence').and.returnValue(of(null)),
      reset: createSpy('reset')
    };
    const routerMock = {
      navigate: createSpy('navigate'),
      navigateByUrl: createSpy('navigateByUrl')
    };
    await TestBed.configureTestingModule({
      declarations: [ RegisterSuccessComponent ],
      imports: [RouterTestingModule, HttpClientTestingModule, FormsModule, ReactiveFormsModule],
      providers: [
        {provide: RegisterService, useValue: registerServiceMock},
        {provide: Router, useValue: routerMock},
      ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    registerService = TestBed.inject(RegisterService);
    router = TestBed.inject(Router);
    fixture = TestBed.createComponent(RegisterSuccessComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should redirect if there are no register credentials provided - email', () => {
    registerService.email = '';

    component.ngOnInit();
    expect(router.navigate).toHaveBeenCalledWith(['/login']);
  });

  it('should redirect if there are no register credentials provided - first Name', () => {
    registerService.firstName = '';

    component.ngOnInit();
    expect(router.navigate).toHaveBeenCalledWith(['/login']);
  });

  it('should redirect if there are no register credentials provided - last Name', () => {
    registerService.lastName = '';

    component.ngOnInit();
    expect(router.navigate).toHaveBeenCalledWith(['/login']);
  });


  it('should redirect if there are no register credentials provided - password', () => {
    registerService.password = '';

    component.ngOnInit();
    expect(router.navigate).toHaveBeenCalledWith(['/login']);
  });

  it('should redirect to login page on onClickSignIn()', () => {
    component.onClickSignIn();

    expect(router.navigate).toHaveBeenCalledWith(['/login']);
  });

  it('should get complete name from registerService', () => {
    expect(component.name).toEqual('Ime Prezime');
  });

});
