import {ComponentFixture, fakeAsync, TestBed, tick} from '@angular/core/testing';

import { ListViewComponent } from './list-view.component';
import {BehaviorSubject, of} from 'rxjs';
import {ConfirmationService, MessageService} from 'primeng/api';
import {HttpClientTestingModule} from '@angular/common/http/testing';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {RouterTestingModule} from '@angular/router/testing';
import {CardModule} from 'primeng/card';
import {ListElementComponent} from '../list-element/list-element.component';
import {CulturalOfferingsService} from '../../core/services/cultural-offerings/cultural-offerings.service';
import {MenuModule} from 'primeng/menu';
import {DialogModule} from 'primeng/dialog';
import {CulturalOffering} from '../../core/models/cultural-offering';
import {ButtonModule} from 'primeng/button';
import {CheckboxModule} from 'primeng/checkbox';
import {SliderModule} from 'primeng/slider';
import {NgSelectModule} from '@ng-select/ng-select';
import {SelectButtonModule} from 'primeng/selectbutton';
import {AutoCompleteModule} from 'primeng/autocomplete';
import {DebugElement} from '@angular/core';
import {By} from '@angular/platform-browser';

describe('ListViewComponent', () => {
  let component: ListViewComponent;
  let fixture: ComponentFixture<ListViewComponent>;
  let culturalOfferingsService: CulturalOfferingsService;

  beforeEach(async () => {
    const mockCulturalOfferings = {
      content: [
          {
            id: 1,
            name: 'CulturalOffering1',
            briefInfo: 'CulturalOfferingInfo1',
            latitude: 10,
            longitude: 5,
            address: '9336 Civic Center Dr, Beverly Hills, CA 90210, USA',
            photoId: 1,
            overallRating: 5,
            numReviews: 2,
            additionalInfo: 'info',
            subcategoryId: 1,
            subcategoryName: 'subcategory',
            numSubscribed: 0,
            categoryName: 'Category1',
            categoryId: 1,
            numPhotos: 0,
            subscribed: false
          },
        {
          id: 2,
          name: 'CulturalOffering2',
          briefInfo: 'CulturalOfferingInfo2',
          latitude: 10,
          longitude: 5,
          address: '9336 Civic Center Dr, Beverly Hills, CA 90210, USA',
          photoId: 1,
          overallRating: 5,
          numReviews: 2,
          additionalInfo: 'info',
          subcategoryId: 1,
          subcategoryName: 'subcategory',
          numSubscribed: 0,
          categoryName: 'Category1',
          categoryId: 1,
          numPhotos: 0,
          subscribed: false
        },
        {
          id: 3,
          name: 'CulturalOffering3',
          briefInfo: 'CulturalOfferingInfo3',
          latitude: 10,
          longitude: 5,
          address: '9336 Civic Center Dr, Beverly Hills, CA 90210, USA',
          photoId: 1,
          overallRating: 5,
          numReviews: 2,
          additionalInfo: 'info',
          subcategoryId: 1,
          subcategoryName: 'subcategory',
          numSubscribed: 0,
          categoryName: 'Category1',
          categoryId: 1,
          numPhotos: 0,
          subscribed: false
        },
        {
          id: 4,
          name: 'CulturalOffering4',
          briefInfo: 'CulturalOfferingInfo4',
          latitude: 10,
          longitude: 5,
          address: '9336 Civic Center Dr, Beverly Hills, CA 90210, USA',
          photoId: 1,
          overallRating: 5,
          numReviews: 2,
          additionalInfo: 'info',
          subcategoryId: 1,
          subcategoryName: 'subcategory',
          numSubscribed: 0,
          categoryName: 'Category1',
          categoryId: 1,
          numPhotos: 0,
          subscribed: false
        },
        ],
      pageable: {
        pageNumber: 1
      },
      totalPages: 5};

    const culturalOfferingsServiceMock = {
      getCulturalOfferings: jasmine.createSpy('getCulturalOfferings').and.returnValue(of(mockCulturalOfferings)),
      unsubscribe: jasmine.createSpy('unsubscribe').and.returnValue(of({})),
      delete: jasmine.createSpy('delete').and.returnValue(of({})),
      searchQuery: new BehaviorSubject<CulturalOffering | undefined>(undefined)

    };

    await TestBed.configureTestingModule({
      declarations: [ListViewComponent, ListElementComponent],
      providers:    [
        {provide: CulturalOfferingsService, useValue: culturalOfferingsServiceMock },
        ConfirmationService,
        MessageService],
      imports: [FormsModule, HttpClientTestingModule, ReactiveFormsModule, FormsModule, RouterTestingModule,
        CardModule, MenuModule, DialogModule, ButtonModule, CheckboxModule, SliderModule, NgSelectModule,
        SelectButtonModule, AutoCompleteModule]
    })
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ListViewComponent);
    component = fixture.componentInstance;
    culturalOfferingsService = TestBed.inject(CulturalOfferingsService);
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should fetch the cultural offers list on init', fakeAsync (() => {
    component.ngOnInit();
    expect(culturalOfferingsService.getCulturalOfferings).toHaveBeenCalled();

    tick();

    fixture.whenStable()
      .then(() => {
        fixture.detectChanges();
        const elements: DebugElement[] =
          fixture.debugElement.queryAll(By.css('.culturalOffer'));
        expect(elements.length).toBe(4);
      });
  }));

  it('should delete offering', () => {
    const spyRestart = spyOn(component, 'resetCulturalOfferings');

    fixture.detectChanges();
    component.onOfferingDeleted();

    expect(spyRestart).toHaveBeenCalled();
  });

  it('should restart cultural offerings', () => {
    component.ngOnInit();
    component.resetCulturalOfferings();
    expect(culturalOfferingsService.getCulturalOfferings).toHaveBeenCalled();
  });

  it('should scroll down', () => {
    component.ngOnInit();
    component.onScrollDown();
    expect(culturalOfferingsService.getCulturalOfferings).toHaveBeenCalled();
  });



  it('should reset filter', () => {
    const spySaveFilter = spyOn(component, 'saveFilter');

    fixture.detectChanges();
    component.resetFilter();

    expect(spySaveFilter).toHaveBeenCalled();
  });

  it('should open filter dialog', () => {
    const spyGetCategories = spyOn(component, 'getCategories');

    fixture.detectChanges();
    component.openFilterDialog();

    expect(spyGetCategories).toHaveBeenCalled();
  });

  it('should set sort type', () => {
    const spyRestart = spyOn(component, 'resetCulturalOfferings');

    fixture.detectChanges();
    component.setSortType('a', 'a');

    expect(spyRestart).toHaveBeenCalled();
  });
});
