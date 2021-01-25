import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ListElementComponent } from './list-element.component';
import {CulturalOffering} from '../../core/models/cultural-offering';
import {Router} from '@angular/router';
import {ConfirmationService, MessageService} from 'primeng/api';
import {DialogService} from 'primeng/dynamicdialog';
import {CulturalOfferingsService} from '../../core/services/cultural-offerings/cultural-offerings.service';
import {of} from 'rxjs';
import {RouterTestingModule} from '@angular/router/testing';
import {AvatarModule} from 'ngx-avatar';
import {HttpClientTestingModule} from '@angular/common/http/testing';
import {DialogModule} from 'primeng/dialog';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {CardModule} from 'primeng/card';
import {PasswordModule} from 'primeng/password';
import {By} from '@angular/platform-browser';
import createSpy = jasmine.createSpy;

describe('ListElementComponent', () => {
  let component: ListElementComponent;
  let fixture: ComponentFixture<ListElementComponent>;
  let router: Router;
  let culturalOfferingsService: CulturalOfferingsService;
  let confirmationService: ConfirmationService;
  let messageService: MessageService;
  let dialogService: DialogService;
  let navigateSpy: any;
  let confirm: any;
  let isLoggedIn: any;
  const event = {
    stopPropagation: createSpy ('stopPropagation')
  };

  beforeEach(async () => {

    const culturalOfferingsServiceMock = {
      subscriptions: jasmine.createSpy('subscriptions').and.returnValue(of({})),
      unsubscribe: jasmine.createSpy('unsubscribe').and.returnValue(of({})),
      delete: jasmine.createSpy('delete').and.returnValue(of({})),
      subscribe: jasmine.createSpy('subscribe').and.returnValue(of({})),

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
      declarations: [ ListElementComponent ],
      providers:    [
        {provide: CulturalOfferingsService, useValue: culturalOfferingsServiceMock },
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
    fixture = TestBed.createComponent(ListElementComponent);
    component = fixture.componentInstance;
    component.culturalOffering =
      {
        id: 1,
        name: 'CulturalOffering',
        briefInfo: 'CulturalOfferingInfo',
        latitude: 10,
        longitude: 5,
        address: '9336 Civic Center Dr, Beverly Hills, CA 90210, USA',
        overallRating: 54,
        numReviews: 0,
        subcategoryId: 1,
        subcategoryName: 'subcategory',
        numSubscribed: 0,
        categoryName: 'Category1',
        categoryId: 1,
      };
    router = TestBed.inject(Router);
    culturalOfferingsService = TestBed.inject(CulturalOfferingsService);
    messageService =  TestBed.inject(MessageService);
    dialogService =  TestBed.inject(DialogService);
    confirmationService = TestBed.inject(ConfirmationService);
    navigateSpy = spyOn(router, 'navigate');
    confirm = spyOn(confirmationService, 'confirm');
    isLoggedIn = spyOn(component, 'isLoggedIn');
    // onClickDelete = spyOn(component, 'onClickDelete').and.returnValue(confirm());
    // onClickEdit = spyOn(component, 'onClickEdit').and.returnValue(navigateSpy());
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should init success',  () => {
    fixture.detectChanges();
    const email = fixture.debugElement.query(By.css('.offering-name')).nativeElement;
    expect(email.textContent.trim()).toEqual('CulturalOffering');
    const cat = fixture.debugElement.query(By.css('.category-subcategory')).nativeElement;
    expect(cat.textContent.trim()).toEqual('Category1 > subcategory');
    const subscribed = fixture.debugElement.query(By.css('.subscribed')).nativeElement;
    expect(subscribed.textContent.trim()).toEqual('0 people subscribed.');
  });

  it('should click delete',  () => {
    fixture.detectChanges();
    component.onClickDelete(event);

    expect(confirm).toHaveBeenCalled();
  });

  it('should click edit',  () => {
    fixture.detectChanges();
    component.onClickEdit(event);

    expect(navigateSpy).toHaveBeenCalled();
  });

  it('should click on card',  () => {
    fixture.detectChanges();
    component.onClickCard();

    expect(navigateSpy).toHaveBeenCalled();
  });

  it('should click unsubscribe',  () => {
    fixture.detectChanges();
    component.onClickUnsubscribe(event);

    expect(culturalOfferingsService.unsubscribe).toHaveBeenCalled();
  });

  it('should click subscribe',  () => {
    fixture.detectChanges();
    component.onClickSubscribe(event);

    expect(isLoggedIn).toHaveBeenCalled();
  });

  it('should click map view',  () => {
    fixture.detectChanges();
    component.onClickViewMap();

    expect(navigateSpy).toHaveBeenCalled();
  });
});
