import {ComponentFixture, fakeAsync, TestBed, tick} from '@angular/core/testing';

import {RegisterVerifyComponent} from './register-verify.component';
import {ActivatedRoute, Router} from '@angular/router';
import {of, throwError} from 'rxjs';
import {RouterTestingModule} from '@angular/router/testing';
import {HttpClientTestingModule} from '@angular/common/http/testing';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {MessageService} from 'primeng/api';
import {VerifyService} from '../../../../core/services/verify/verify.service';
import createSpy = jasmine.createSpy;
import {ButtonModule} from 'primeng/button';

describe('RegisterVerifyComponent', () => {
  let component: RegisterVerifyComponent;
  let fixture: ComponentFixture<RegisterVerifyComponent>;
  let verifyService: VerifyService;
  let router: Router;
  let activatedRoute: ActivatedRoute;
  let addSpy: any;

  beforeEach(async () => {
    const verifyServiceMock = {
      name: '',
      checkExistence: createSpy('checkExistence').and.returnValue(of({value: 'Ime'})),
      verify: createSpy('verify').and.returnValue(of(null))
    };
    const routerMock = {
      navigate: createSpy('navigate'),
    };
    const activatedRouteMock = {params: of({id: '1'})};
    await TestBed.configureTestingModule({
      declarations: [ RegisterVerifyComponent ],
      imports: [HttpClientTestingModule, ButtonModule],
      providers: [
        MessageService,
        {provide: VerifyService, useValue: verifyServiceMock},
        {provide: Router, useValue: routerMock},
        {provide: ActivatedRoute, useValue: activatedRouteMock}
      ]
    })
      .compileComponents();
  });

  beforeEach(() => {
    verifyService = TestBed.inject(VerifyService);
    router = TestBed.inject(Router);
    activatedRoute = TestBed.inject(ActivatedRoute);
    const messageService = TestBed.inject(MessageService);
    addSpy = spyOn(Object.getPrototypeOf(messageService), 'add');

    fixture = TestBed.createComponent(RegisterVerifyComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });


  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should navigate to login if no id was specified', fakeAsync(() => {
    (component as any).activatedRoute = {params: of({})};

    component.ngOnInit();

    tick();

    expect(router.navigate).toHaveBeenCalledWith(['/login']);

  }));

  it('should set id on ngInit', fakeAsync(() => {

    component.ngOnInit();

    tick();
    expect(router.navigate).not.toHaveBeenCalled();
    expect(component.id).toEqual('1');
    expect(verifyService.checkExistence).toHaveBeenCalledWith('1');
    expect(verifyService.name).toEqual('Ime');
  }));

  it('should set user to verified', () => {
    component.onClickVerify();

    expect(verifyService.verify).toHaveBeenCalledWith('1');
    expect(component.verified).toBeTrue();
  });

  it('should get error from verify service', () => {
    verifyService.verify = createSpy('verify').and.returnValue(throwError(null));

    component.onClickVerify();

    expect(addSpy).toHaveBeenCalled();
  });

  it('should get name from verifyservice', () => {
    expect(component.name).toEqual(verifyService.name);
  });
});
