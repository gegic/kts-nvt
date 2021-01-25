import {ComponentFixture, fakeAsync, TestBed, tick} from '@angular/core/testing';

import { EnterEmailComponent } from './enter-email.component';
import {RouterTestingModule} from '@angular/router/testing';
import createSpy = jasmine.createSpy;
import {of, Subscription} from 'rxjs';
import {LoginService} from '../../../../core/services/login/login.service';
import {ActivatedRoute, Router} from '@angular/router';
import {MessageService} from 'primeng/api';
import {HttpClientTestingModule} from '@angular/common/http/testing';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {By} from '@angular/platform-browser';

describe('EnterEmailComponent', () => {
  let component: EnterEmailComponent;
  let fixture: ComponentFixture<EnterEmailComponent>;
  let loginService: LoginService;
  let router: Router;
  let mockAdd: any;

  beforeEach(async () => {
    const loginServiceMock = {
      email: '',
      name: '',
      checkExistence: createSpy('checkExistence').and.returnValue(of({value: 'Haris Gegic'})),
      reset: createSpy('reset')
    };
    const routerMock = {
      navigate: createSpy('navigate')
    };
    const activatedRouteMock = {};
    await TestBed.configureTestingModule({
      declarations: [ EnterEmailComponent ],
      imports: [HttpClientTestingModule, FormsModule, ReactiveFormsModule],
      providers: [
        MessageService,
        {provide: LoginService, useValue: loginServiceMock},
        {provide: Router, useValue: routerMock},
        {provide: ActivatedRoute, useValue: activatedRouteMock}
      ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    loginService = TestBed.inject(LoginService);
    router = TestBed.inject(Router);
    const messageService = TestBed.inject(MessageService);
    mockAdd = spyOn(Object.getPrototypeOf(messageService), 'add');

    fixture = TestBed.createComponent(EnterEmailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should proceed when onClickProceed', fakeAsync(() => {

    component.emailControl.setValue('somemail@mail.com');
    component.onClickProceed();

    tick();
    fixture.detectChanges();

    expect(loginService.checkExistence).toHaveBeenCalledWith('somemail@mail.com');
    expect(loginService.email).toEqual('somemail@mail.com');
    expect(loginService.name).toEqual('Haris Gegic');
    expect(router.navigate).toHaveBeenCalledWith(jasmine.arrayContaining(['./password']), jasmine.anything());
  }));

  it('should navigate to another page after the onClickNewAccount()', () => {
    component.onClickNewAccount();

    expect(router.navigate).toHaveBeenCalledWith(['/register']);
    expect(loginService.reset).toHaveBeenCalled();
  });

  it('should unsubscribe on destroy', fakeAsync(() => {
    component.onClickProceed();

    tick();
    fixture.detectChanges();

    component.ngOnDestroy();
    expect((component as any).subscription.closed).toBeTrue();
  }));
});
