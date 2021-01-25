import {ComponentFixture, fakeAsync, TestBed, tick} from '@angular/core/testing';

import { CulturalOfferingAddComponent } from './cultural-offering-add.component';
import {DialogService, DynamicDialogRef} from 'primeng/dynamicdialog';
import {
  NominatimPlace,
  PlaceOfferingService,
  ToAddressJson
} from '../../core/services/place-offering/place-offering.service';
import {BehaviorSubject, of} from 'rxjs';
import {Place} from '../../core/models/place';
import createSpy = jasmine.createSpy;
import {AddOfferingService} from '../../core/services/add-offering/add-offering.service';
import {MessageService} from 'primeng/api';
import Spy = jasmine.Spy;
import {RouterTestingModule} from '@angular/router/testing';
import {ActivatedRoute, Router} from '@angular/router';
import {HttpClientTestingModule} from '@angular/common/http/testing';
import {NgSelectModule} from '@ng-select/ng-select';
import {AutoCompleteModule} from 'primeng/autocomplete';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {InputTextareaModule} from 'primeng/inputtextarea';
import {ButtonModule} from 'primeng/button';
import {Category} from '../../core/models/category';
import {CulturalOffering} from '../../core/models/cultural-offering';
import {DomEvent} from 'leaflet';
import off = DomEvent.off;
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import * as L from 'leaflet';
import {Component} from '@angular/core';

describe('CulturalOfferingAddComponent', () => {

  let component: CulturalOfferingAddComponent;
  let fixture: ComponentFixture<CulturalOfferingAddComponent>;
  let dialogService: DialogService;
  let placeOfferingService: PlaceOfferingService;
  let addOfferingService: AddOfferingService;
  let spyAdd: Spy;
  let spyNavigate: Spy;
  let activatedRoute: ActivatedRoute;

  beforeEach(async () => {
    const recommendations: NominatimPlace[] = getRecommendations();

    const placeOfferingServiceMock = {
      place: new BehaviorSubject<Place>(new Place(null, false)),
      latLng: new BehaviorSubject<L.LatLng | null>(null),
      reset: createSpy('reset'),
      getRecommendations: createSpy('getRecommendations').and.returnValue(of(recommendations)),
      recommendationSelected: createSpy('recommendationSelected').and.callThrough(),
      getAddress: createSpy('getAddress').and.returnValue(of({display_name: 'Adresa'}))
    };

    const activatedRouteMock = {
      snapshot: {
        data: {mode: 'add'},
        // params: {id: 1}
      }
    };

    await TestBed.configureTestingModule({
      declarations: [ CulturalOfferingAddComponent ],
      imports: [
        RouterTestingModule,
        HttpClientTestingModule,
        NgSelectModule,
        AutoCompleteModule,
        FormsModule,
        ReactiveFormsModule,
        InputTextareaModule,
        ButtonModule,
        BrowserAnimationsModule
      ],
      providers: [
        DialogService,
        {provide: PlaceOfferingService, useValue: placeOfferingServiceMock},
        AddOfferingService,
        MessageService,
        {provide: ActivatedRoute, useValue: activatedRouteMock}
      ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    dialogService = TestBed.inject(DialogService);
    placeOfferingService = TestBed.inject(PlaceOfferingService);
    addOfferingService = TestBed.inject(AddOfferingService);
    spyAdd = spyOn(TestBed.inject(MessageService), 'add');
    spyNavigate = spyOn(TestBed.inject(Router), 'navigate');
    activatedRoute = TestBed.inject(ActivatedRoute);
    fixture = TestBed.createComponent(CulturalOfferingAddComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should setup on init for addition', fakeAsync(() => {
    const spyGetCategories = spyOn(component, 'getCategories');
    component.ngOnInit();

    tick();

    fixture.detectChanges();
    expect(spyGetCategories).toHaveBeenCalledWith();
    expect(component.mode).toEqual('add');

    component.formGroup.get('address')?.setValue({display_name: 'adresa'});

    tick();
    fixture.detectChanges();

    expect(placeOfferingService.reset).toHaveBeenCalled();
    expect(component.mapSet).toBeFalse();

    const spyCategoryChosen = spyOn(component, 'categoryChosen');
    const category: Category = {
      id: 1,
      name: 'Kategorijica'
    };
    component.formGroup.get('selectedCategory')?.setValue(category);

    tick();
    fixture.detectChanges();

    expect(spyCategoryChosen).toHaveBeenCalled();

  }));

  it('should setup on init for editing', fakeAsync(() => {
    (component as any).activatedRoute.snapshot = {
      data: {mode: 'edit'},
      params: {id: 1}
    };
    const spyGetCategories = spyOn(component, 'getCategories');

    const offering = getCulturalOfferings()[0];
    const spyGetOffering = spyOn(addOfferingService, 'getOffering').and
      .returnValue(of(offering));

    component.ngOnInit();

    tick();

    fixture.detectChanges();
    expect(spyGetCategories).toHaveBeenCalledWith();
    expect(component.mode).toEqual('edit');
    expect(spyGetOffering).toHaveBeenCalledWith(1);
    expect(addOfferingService.culturalOffering).toEqual(offering);
    expect(component.formGroup.get('name').value).toEqual(offering.name);
    expect(component.formGroup.get('briefInfo').value).toEqual(offering.briefInfo);
    expect(component.formGroup.get('additionalInfo').value).toEqual(offering.additionalInfo);
    expect(component.formGroup.get('address').value).toEqual({display_name: offering.address});
    expect(component.formGroup.get('selectedCategory').value)
      .toEqual({id: offering.categoryId, name: offering.categoryName});
    expect(component.formGroup.get('selectedSubcategory').value)
      .toEqual({id: offering.subcategoryId, name: offering.subcategoryName});

    expect(component.formGroup.get('selectedSubcategory').disabled).toBeTrue();
    expect(component.photo.id).toEqual(1);

    component.formGroup.get('address')?.setValue({display_name: 'adresa'});

    tick();
    fixture.detectChanges();

    expect(placeOfferingService.reset).toHaveBeenCalled();
    expect(component.mapSet).toBeFalse();

    const spyCategoryChosen = spyOn(component, 'categoryChosen');
    const category: Category = {
      id: 1,
      name: 'Kategorijica'
    };
    component.formGroup.get('selectedCategory')?.setValue(category);

    tick();
    fixture.detectChanges();

    expect(spyCategoryChosen).toHaveBeenCalled();

  }));

  it('should show mapdialog and select a position', fakeAsync(() => {
    const dialogRef = new DynamicDialogRef();
    const spyOpen = spyOn(dialogService, 'open').and.returnValue(dialogRef);

    component.showMapDialog();

    tick();
    fixture.detectChanges();

    expect(spyOpen).toHaveBeenCalled();
    tick();
    fixture.detectChanges();

    dialogRef.close({address: 'Adresa', coordinates: [10, 11]});

    tick();
    fixture.detectChanges();

    expect(component.mapSet).toBeTrue();
    expect(component.formGroup.get('address')).toBeDefined();
    expect(component.formGroup.get('address')?.value?.display_name).toEqual('Adresa');
    expect(addOfferingService.coordinates).toEqual([10, 11]);
  }));

  it('should get address', fakeAsync(() => {
    const event = {query: 'place'};

    component.getAddress(event);

    expect(placeOfferingService.getRecommendations).toHaveBeenCalledWith('place');
    expect(component.recommendations).toEqual(getRecommendations());
  }));

  it('should select an address', fakeAsync(() => {

  }));
});

function getCulturalOfferings(): CulturalOffering[] {
  return  [
    {
      id: 1,
      latitude: 13,
      longitude: 15,
      name: 'My offering',
      briefInfo: 'Some info',
      address: 'Svetozara Markovica',
      numSubscribed: 0,
      subscribed: false,
      subcategoryId: 1,
      categoryId: 1,
      subcategoryName: 'Vasar',
      categoryName: 'Manifestacija',
      overallRating: 0,
      numReviews: 0,
      numPhotos: 0,
      photoId: 1
    },
    {
      id: 2,
      latitude: 13.4,
      longitude: 15.1,
      name: 'My second offering',
      briefInfo: 'Some info',
      address: 'Svetozara Miletica',
      numSubscribed: 1,
      subscribed: false,
      subcategoryId: 1,
      categoryId: 1,
      subcategoryName: 'Vasar',
      categoryName: 'Manifestacija',
      overallRating: 0,
      numReviews: 0,
      numPhotos: 0,
      photoId: 2
    }
  ];
}

function getRecommendations(): NominatimPlace[] {
  return [
    {place_id: 13, lon: 10, lat: 11, display_name: 'Place1'},
    {place_id: 14, lon: 11, lat: 11, display_name: 'Place2'},
    {place_id: 15, lon: 13, lat: 11, display_name: 'Place3'},
    {place_id: 16, lon: 14, lat: 11, display_name: 'Place4'},
  ];
}
