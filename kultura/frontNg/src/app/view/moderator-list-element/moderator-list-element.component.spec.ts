import {ComponentFixture, TestBed} from '@angular/core/testing';

import { ModeratorListElementComponent } from './moderator-list-element.component';
import {Router} from '@angular/router';
import {ModeratorService} from '../../core/services/moderator/moderator.service';
import {ConfirmationService, MessageService} from 'primeng/api';
import {DialogService} from 'primeng/dynamicdialog';
import {Moderator} from '../../core/models/moderator';
import {of} from 'rxjs';
import {DialogModule} from 'primeng/dialog';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {CardModule} from 'primeng/card';
import {PasswordModule} from 'primeng/password';
import {AvatarModule} from 'ngx-avatar';
import {HttpClientTestingModule} from '@angular/common/http/testing';
import {By} from '@angular/platform-browser';
import {RouterTestingModule} from '@angular/router/testing';

describe('ModeratorListElementComponent', () => {
  let component: ModeratorListElementComponent;
  let fixture: ComponentFixture<ModeratorListElementComponent>;
  let router: Router;
  let moderatorService: ModeratorService;
  let confirmationService: ConfirmationService;
  let messageService: MessageService;
  let dialogService: DialogService;
  let navigateSpy: any;
  // let onClickEdit: any;


  beforeEach(async () => {
    const moderator: Moderator = {
      id: '1',
      email: 'test1@mail.com',
      firstName: 'Firstname1',
      lastName: 'Lastname1'
    };

    const moderatorServiceMocked = {
      delete: jasmine.createSpy('createModerator').and.returnValue(of(moderator))
    };

    const messageServiceMocked = jasmine.createSpyObj(
      'MessageService',
      ['add'],
      ['messages']
    );

    const confirmationServiceMocked = {
      confirm: jasmine.createSpy('confirm').and.returnValue(of({}))
    };
    const dialogServiceMocked = {
      open: jasmine.createSpy('open').and.returnValue(of({}))
    };

    await TestBed.configureTestingModule({
      declarations: [ ModeratorListElementComponent ],
      providers:    [
        {provide: ModeratorService, useValue: moderatorServiceMocked },
        { provide: MessageService, useValue: messageServiceMocked },
        { provide: DialogService, useValue: dialogServiceMocked},
        {provide: ConfirmationService, useValue: confirmationServiceMocked},
        ConfirmationService,
        MessageService],
      imports: [RouterTestingModule, AvatarModule, HttpClientTestingModule, DialogModule, ReactiveFormsModule,
        FormsModule, CardModule, PasswordModule]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ModeratorListElementComponent);
    component = fixture.componentInstance;
    component.moderator = {
      id: '1',
      email: 'test1@mail.com',
      firstName: 'Firstname1',
      lastName: 'Lastname1'
    };
    router = TestBed.inject(Router);
    moderatorService = TestBed.inject(ModeratorService);
    messageService =  TestBed.inject(MessageService);
    dialogService =  TestBed.inject(DialogService);
    confirmationService = TestBed.inject(ConfirmationService);
    navigateSpy = spyOn(router, 'navigate');
    // onClickEdit = spyOn(component, 'onClickEdit').and.returnValue(navigateSpy());
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should init success',  () => {
    fixture.detectChanges();
    const email = fixture.debugElement.query(By.css('.email')).nativeElement;
    expect(email.textContent.trim()).toContain('Mail address: test1@mail.com');
    const name = fixture.debugElement.query(By.css('.name')).nativeElement;
    expect(name.textContent.trim()).toContain('Firstname1 Lastname1');
  });

  it('should delete',  () => {
    const confirm = spyOn(confirmationService, 'confirm');
    fixture.detectChanges();
    component.onClickDelete();

    expect(confirm).toHaveBeenCalled();
  });

  it('should edit',  () => {
    fixture.detectChanges();
    component.onClickEdit();

    expect(navigateSpy).toHaveBeenCalled();
  });
});
