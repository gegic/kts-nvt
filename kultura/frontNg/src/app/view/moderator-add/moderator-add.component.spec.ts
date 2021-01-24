import {ComponentFixture, fakeAsync, TestBed} from '@angular/core/testing';

import {ModeratorAddComponent} from './moderator-add.component';
import {Router} from '@angular/router';
import {ModeratorService} from '../../core/services/moderator/moderator.service';
import {ConfirmationService, MessageService} from 'primeng/api';
import {DialogService} from 'primeng/dynamicdialog';
import {Moderator} from '../../core/models/moderator';
import {of, throwError} from 'rxjs';
import {DialogModule} from 'primeng/dialog';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {CardModule} from 'primeng/card';

describe('ModeratorAddComponent', () => {
  let component: ModeratorAddComponent;
  let fixture: ComponentFixture<ModeratorAddComponent>;
  let router: Router;
  let moderatorService: ModeratorService;
  let messageService: MessageService;
  let dialogService: DialogService;

  beforeEach(async () => {
    const moderator: Moderator = {
      id: '1',
      email: 'test1@mail.com',
      firstName: 'Firstname1',
      lastName: 'Lastname1'
    };

    const moderatorServiceMocked = {
      createModerator: jasmine.createSpy('createModerator').and.returnValue(of(moderator))
    };

    const messageServiceMocked = jasmine.createSpyObj(
      'MessageService',
      ['add'],
      ['messages']
    );
    const dialogServiceMocked = {
      open: jasmine.createSpy('open').and.returnValue(of({}))
    };

    const routerMock = {
      navigate: jasmine.createSpy('navigate')
    };


    await TestBed.configureTestingModule({
      declarations: [ModeratorAddComponent],
      providers:    [
        {provide: ModeratorService, useValue: moderatorServiceMocked },
        { provide: MessageService, useValue: messageServiceMocked },
        { provide: DialogService, useValue: dialogServiceMocked},
        {provide: ConfirmationService, useValue: ConfirmationService},
        {provide: Router, useValue: routerMock}],
      imports: [DialogModule, ReactiveFormsModule, FormsModule, CardModule]
    })
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ModeratorAddComponent);
    component = fixture.componentInstance;
    router = TestBed.inject(Router);

    messageService =  TestBed.inject(MessageService);
    dialogService =  TestBed.inject(DialogService);

    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should be invalid form when submitted and last name is empty', () => {
    fixture = TestBed.createComponent(ModeratorAddComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    router = TestBed.inject(Router);
    moderatorService = TestBed.inject(ModeratorService);
    messageService =  TestBed.inject(MessageService);
    dialogService =  TestBed.inject(DialogService);
    component.ngOnInit();

    component.moderatorForm.controls.firstName.setValue('Miar');
    component.moderatorForm.controls.lastName.setValue('');
    component.moderatorForm.controls.email.setValue('test@mail.com');
    component.moderatorForm.controls.password.setValue('miratMiric123');
    component.moderatorForm.controls.repeatPassword.setValue('miratMiric123');
    component.onSubmit();

    expect(component.moderatorForm.invalid).toBeTruthy();
    expect(moderatorService.createModerator).toHaveBeenCalledTimes(0);

    /*const errorMsg = fixture.debugElement.query(By.css('#toast-container'));
    expect(errorMsg).toBeDefined();*/
  });

  it('should be initialized', () => {
    fixture = TestBed.createComponent(ModeratorAddComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    router = TestBed.inject(Router);
    moderatorService = TestBed.inject(ModeratorService);
    messageService =  TestBed.inject(MessageService);
    dialogService =  TestBed.inject(DialogService);
    component.ngOnInit();

    expect(component.moderatorForm).toBeDefined();
    expect(component.moderatorForm.invalid).toBeTruthy();
  });

  it('should be invalid form when submitted and first name is empty', () => {
    fixture = TestBed.createComponent(ModeratorAddComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    router = TestBed.inject(Router);
    moderatorService = TestBed.inject(ModeratorService);
    messageService =  TestBed.inject(MessageService);
    dialogService =  TestBed.inject(DialogService);
    component.ngOnInit();

    component.moderatorForm.controls.firstName.setValue('');
    component.moderatorForm.controls.lastName.setValue('Miric');
    component.moderatorForm.controls.email.setValue('test@mail.com');
    component.moderatorForm.controls.password.setValue('miratMiric123');
    component.moderatorForm.controls.repeatPassword.setValue('miratMiric123');
    component.onSubmit();

    expect(component.moderatorForm.invalid).toBeTruthy();
    expect(moderatorService.createModerator).toHaveBeenCalledTimes(0);

    /*const errorMsg = fixture.debugElement.query(By.css('#toast-container'));
    expect(errorMsg).toBeDefined();*/
  });

  it('should add moderator successfully when submitted', fakeAsync(() => {
    fixture = TestBed.createComponent(ModeratorAddComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    router = TestBed.inject(Router);
    moderatorService = TestBed.inject(ModeratorService);
    messageService =  TestBed.inject(MessageService);
    dialogService =  TestBed.inject(DialogService);
    component.ngOnInit();

    component.moderatorForm.controls.firstName.setValue('Mitar');
    component.moderatorForm.controls.lastName.setValue('Miric');
    component.moderatorForm.controls.email.setValue('test@mail.com');
    component.moderatorForm.controls.password.setValue('miratMiric123');
    component.moderatorForm.controls.repeatPassword.setValue('miratMiric123');
    component.onSubmit();

    expect(component.moderatorForm.invalid).toBeFalse();
    expect(moderatorService.createModerator).toHaveBeenCalledTimes(1);
  }));

  it('should be invalid form when submitted and passwords incorrect', fakeAsync(() => {
    fixture = TestBed.createComponent(ModeratorAddComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    router = TestBed.inject(Router);
    moderatorService = TestBed.inject(ModeratorService);
    messageService =  TestBed.inject(MessageService);
    dialogService =  TestBed.inject(DialogService);
    component.ngOnInit();

    component.moderatorForm.controls.firstName.setValue('Mitar');
    component.moderatorForm.controls.lastName.setValue('Miric');
    component.moderatorForm.controls.email.setValue('test@mail.com');
    component.moderatorForm.controls.password.setValue('');
    component.moderatorForm.controls.repeatPassword.setValue('miratMiric123');
    component.onSubmit();

    expect(component.moderatorForm.invalid).toBeTruthy();
    expect(moderatorService.createModerator).toHaveBeenCalledTimes(0);
  }));

  it('should be invalid form when submitted and mail incorrect', fakeAsync(() => {
    fixture = TestBed.createComponent(ModeratorAddComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    router = TestBed.inject(Router);
    moderatorService = TestBed.inject(ModeratorService);
    messageService =  TestBed.inject(MessageService);
    dialogService =  TestBed.inject(DialogService);
    component.ngOnInit();

    component.moderatorForm.controls.firstName.setValue('Mitar');
    component.moderatorForm.controls.lastName.setValue('Miric');
    component.moderatorForm.controls.email.setValue('test');
    component.moderatorForm.controls.password.setValue('');
    component.moderatorForm.controls.repeatPassword.setValue('miratMiric123');
    component.onSubmit();

    expect(component.moderatorForm.invalid).toBeTruthy();
    expect(moderatorService.createModerator).toHaveBeenCalledTimes(0);
  }));

  it('should be invalid form when submitted and mail exist and wrong password', fakeAsync(() => {
    const moderatorServiceMailExists = {
      add: jasmine.createSpy('add').and.returnValue(throwError({
        status: 409,
        error: 'Email already exists.',
        message: 'Email already exists.',
      }))
    };
    fixture = TestBed.createComponent(ModeratorAddComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    router = TestBed.inject(Router);
    moderatorService = TestBed.inject(ModeratorService);
    messageService =  TestBed.inject(MessageService);
    dialogService =  TestBed.inject(DialogService);
    moderatorService = TestBed.inject(ModeratorService);
    component.ngOnInit();

    component.moderatorForm.controls.firstName.setValue('Mitar');
    component.moderatorForm.controls.lastName.setValue('Miric');
    component.moderatorForm.controls.email.setValue('test');
    component.moderatorForm.controls.password.setValue('');
    component.moderatorForm.controls.repeatPassword.setValue('miratMiric123');
    component.onSubmit();

    expect(component.moderatorForm.invalid).toBeTruthy();
    expect(moderatorService.createModerator).toHaveBeenCalledTimes(0);
    expect(messageService.add).toHaveBeenCalledTimes(2);
  }));


});
