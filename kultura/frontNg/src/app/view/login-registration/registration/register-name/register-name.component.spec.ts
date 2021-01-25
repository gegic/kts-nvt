import {ComponentFixture, fakeAsync, TestBed} from '@angular/core/testing';

import { RegisterNameComponent } from './register-name.component';
import {RegisterService} from '../../../../core/services/register/register.service';
import {ActivatedRoute, Router} from '@angular/router';
import {of} from 'rxjs';
import {EnterEmailComponent} from '../../login/enter-email/enter-email.component';
import {RouterTestingModule} from '@angular/router/testing';
import {HttpClientTestingModule} from '@angular/common/http/testing';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {MessageService} from 'primeng/api';
import {RegisterEmailComponent} from '../register-email/register-email.component';
import createSpy = jasmine.createSpy;

describe('RegisterNameComponent', () => {
  let component: RegisterNameComponent;
  let fixture: ComponentFixture<RegisterNameComponent>;
  let registerService: RegisterService;
  let router: Router;
  let addSpy: any;

  beforeEach(async () => {
    const registerServiceMock = {
      firstName: '',
      lastName: ''
    };
    const routerMock = {
      navigate: createSpy('navigate')
    };
    const activatedRouteMock = {};
    await TestBed.configureTestingModule({
      declarations: [ RegisterNameComponent ],
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
    registerService.email = 'email@mail.com';
    fixture = TestBed.createComponent(RegisterNameComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should go to the previous page if there\'s no email in registerService', () => {
    registerService.email = '';
    component.ngOnInit();

    expect(router.navigate).toHaveBeenCalledWith(jasmine.arrayContaining(['..']), jasmine.anything());
  });

  it('should pass if there\'s email in registerService', () => {
    component.ngOnInit();
    expect(router.navigate).not.toHaveBeenCalled();
  });

  it('should proceed on onClickProceed()', fakeAsync(() => {
    component.nameGroup.setValue({firstName: 'Ime', lastName: 'Prezime'});

    component.onClickProceed();

    expect(registerService.firstName).toEqual('Ime');
    expect(registerService.lastName).toEqual('Prezime');

    expect(router.navigate).toHaveBeenCalledWith(jasmine.arrayContaining(['../password']), jasmine.anything());
  }));

  it('should not proceed because names are incorrect on onClickProceed()', fakeAsync(() => {
    component.nameGroup.setValue({firstName: 'iMe', lastName: 'prezime'});

    component.onClickProceed();

    expect(addSpy).toHaveBeenCalled();
  }));

  it('should navigate back to the previous page', () => {
    component.onClickBack();

    expect(router.navigate).toHaveBeenCalledWith(jasmine.arrayContaining(['..']), jasmine.anything());
  });
});
