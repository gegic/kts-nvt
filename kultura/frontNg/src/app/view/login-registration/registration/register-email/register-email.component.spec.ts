import {ComponentFixture, fakeAsync, TestBed, tick} from '@angular/core/testing';

import { RegisterEmailComponent } from './register-email.component';
import {LoginService} from '../../../../core/services/login/login.service';
import {ActivatedRoute, Router} from '@angular/router';
import {of, throwError} from 'rxjs';
import {EnterEmailComponent} from '../../login/enter-email/enter-email.component';
import {RouterTestingModule} from '@angular/router/testing';
import {HttpClientTestingModule} from '@angular/common/http/testing';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {MessageService} from 'primeng/api';
import {RegisterService} from '../../../../core/services/register/register.service';
import createSpy = jasmine.createSpy;

describe('RegisterEmailComponent', () => {
  let component: RegisterEmailComponent;
  let fixture: ComponentFixture<RegisterEmailComponent>;
  let registerService: RegisterService;
  let router: Router;
  let addSpy: any;

  beforeEach(async () => {
    const registerServiceMock = {
      email: '',
      checkExistence: createSpy('checkExistence').and.returnValue(of(null)),
      reset: createSpy('reset')
    };
    const routerMock = {
      navigate: createSpy('navigate'),
      navigateByUrl: createSpy('navigateByUrl')
    };
    const activatedRouteMock = {};
    await TestBed.configureTestingModule({
      declarations: [ RegisterEmailComponent ],
      imports: [HttpClientTestingModule, FormsModule, ReactiveFormsModule],
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

    fixture = TestBed.createComponent(RegisterEmailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should not proceed onClickProceed()', fakeAsync(() => {
    component.emailControl.setValue('email@mail.com');
    const emailResetSpy = spyOn(component.emailControl, 'reset');

    component.onClickProceed();

    tick();

    expect(addSpy).toHaveBeenCalled();
    expect(emailResetSpy).toHaveBeenCalled();
  }));

  it('should proceed onClickProceed()', fakeAsync(() => {
    registerService.checkExistence = createSpy('checkExistence')
      .and.returnValue(throwError(null));

    component.emailControl.setValue('email@mail.com');

    component.onClickProceed();

    tick();

    expect(registerService.email).toEqual('email@mail.com');
    expect(router.navigate).toHaveBeenCalledWith(jasmine.arrayContaining(['./name']), jasmine.anything());

  }));

  it('should sign in on onClickSignIn()', () => {
    component.onClickSignIn();

    expect(registerService.reset).toHaveBeenCalled();
    expect(router.navigateByUrl).toHaveBeenCalledWith('/login');
  });

  it('should unsubscribe on destroy', fakeAsync(() => {
    component.emailControl.setValue('email@mail.com');
    component.onClickProceed();

    tick();
    fixture.detectChanges();

    component.ngOnDestroy();
    expect((component as any).subscription.closed).toBeTrue();
  }));
});
