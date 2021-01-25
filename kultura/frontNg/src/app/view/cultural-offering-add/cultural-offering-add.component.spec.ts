import {ComponentFixture, fakeAsync, TestBed, tick} from '@angular/core/testing';

import { CulturalOfferingAddComponent } from './cultural-offering-add.component';
import {DialogService, DynamicDialogRef} from 'primeng/dynamicdialog';
import {
  NominatimPlace,
  PlaceOfferingService,
  ToAddressJson
} from '../../core/services/place-offering/place-offering.service';
import {BehaviorSubject, of, throwError} from 'rxjs';
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
import {Subcategory} from '../../core/models/subcategory';
import {CulturalOfferingPhoto} from '../../core/models/culturalOfferingPhoto';

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
      recommendationSelected: createSpy('recommendationSelected'),
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

    tick();
    fixture.detectChanges();

    expect(placeOfferingService.getRecommendations).toHaveBeenCalledWith('place');
    expect(component.recommendations).toEqual(getRecommendations());
  }));

  it('should select an address', fakeAsync(() => {
    const place1 = getRecommendations()[0];
    component.addressSelected(place1);

    tick();
    fixture.detectChanges();

    expect(placeOfferingService.recommendationSelected).toHaveBeenCalled();
    expect(addOfferingService.coordinates).toEqual([place1.lat, place1.lon]);
    expect(component.mapSet).toBeTrue();
  }));

  it('should reset address if it lost focus before selection', fakeAsync(() => {
    component.mapSet = false;
    tick();
    fixture.detectChanges();

    component.addressLostFocus();

    tick();
    fixture.detectChanges();
    expect(component.formGroup.get('address').value).toBeFalsy();
    expect(component.mapSet).toBeFalse();
    expect(placeOfferingService.reset).toHaveBeenCalled();
  }));

  it('should get categories', fakeAsync(() => {

    const spyGetCategories = spyOn(addOfferingService, 'getCategories').and
      .returnValue(of({content: getCategories(), pageable: {pageNumber: 0}, totalPages: 1}));

    component.lastLoadedPage.categories = -1;
    component.totalPages.categories = 0;

    component.getCategories();

    tick();
    fixture.detectChanges();

    expect(spyGetCategories).toHaveBeenCalledWith(0);
    expect(addOfferingService.categories).toEqual(getCategories());
    expect(component.lastLoadedPage.categories).toEqual(0);
    expect(component.totalPages.categories).toEqual(1);
    expect(component.categoriesLoading).toBeFalse();
  }));

  it('should call reset and get subcategories methods', () => {
    const spyReset = spyOn(component, 'resetSubcategories');
    const spyGet = spyOn(component, 'getSubcategories');

    component.categoryChosen(1);

    expect(spyReset).toHaveBeenCalled();
    expect(spyGet).toHaveBeenCalledWith(1);
  });

  it('should reset subcategories', fakeAsync(() => {
    component.resetSubcategories();

    tick();
    fixture.detectChanges();

    expect(component.formGroup.get('selectedSubcategory').value).toBeFalsy();
    expect(addOfferingService.subcategories.length).toEqual(0);
    expect(component.totalPages.subcategories).toEqual(0);
    expect(component.lastLoadedPage.subcategories).toEqual(-1);
  }));

  it('should get subcategories', fakeAsync(() => {
    const spyGetSubcategories = spyOn(addOfferingService, 'getSubcategories').and
      .returnValue(of({content: getSubcategories(), pageable: {pageNumber: 0}, totalPages: 1}));

    component.lastLoadedPage.subcategories = -1;
    component.totalPages.subcategories = 0;
    component.getSubcategories(1);

    tick();
    fixture.detectChanges();

    expect(spyGetSubcategories).toHaveBeenCalledWith(1, 0);
    expect(component.formGroup.get('selectedSubcategory').disabled).toBeFalse();
    expect(addOfferingService.subcategories).toEqual(getSubcategories());
    expect(component.lastLoadedPage.subcategories).toEqual(0);
    expect(component.totalPages.subcategories).toEqual(1);
    expect(component.subcategoriesLoading).toBeFalse();
  }));

  it('should choose a file', fakeAsync(() => {
    const photo: CulturalOfferingPhoto = {id: 1, hovering: false, culturalOfferingId: 1};
    const spyAddPhoto = spyOn(addOfferingService, 'addPhoto').and
      .returnValue(of(photo));

    const blob = new Blob([''], { type: 'text/html' });
    blob[`lastModifiedDate`] = '';
    blob[`name`] = 'photo';
    const fakeFile = blob as File;


    const event = {
      target: {files: [fakeFile]}
    };
    component.fileChosen(event);

    tick();
    fixture.detectChanges();

    expect(spyAddPhoto).toHaveBeenCalledWith(jasmine.any(Blob));
    expect(component.fileLoading).toBeFalse();
    expect(component.photo).toEqual(photo);
  }));

  it('should fail to upload a file', fakeAsync(() => {
    const spyAddPhoto = spyOn(addOfferingService, 'addPhoto').and
      .returnValue(throwError(null));

    const blob = new Blob([''], { type: 'text/html' });
    blob[`lastModifiedDate`] = '';
    blob[`name`] = 'photo';
    const fakeFile = blob as File;


    const event = {
      target: {files: [fakeFile]}
    };
    component.fileChosen(event);

    tick();
    fixture.detectChanges();

    expect(spyAddPhoto).toHaveBeenCalledWith(jasmine.any(Blob));
    expect(component.fileLoading).toBeFalse();
    expect(spyAdd).toHaveBeenCalled();
  }));

  it('should save offering', fakeAsync(() => {
    const photo: CulturalOfferingPhoto = {id: 1, hovering: false, culturalOfferingId: 1};
    const spyAddOffering = spyOn(addOfferingService, 'addOffering').and
      .returnValue(of({id: 1}));
    component.photo = photo;

    component.formGroup.patchValue({
      name: 'Ime',
      briefInfo: 'Info',
      address: {display_name: 'Adresa'},
      selectedCategory: {id: 1, name: 'Kat1'},
      selectedSubcategory: {id: 1, name: 'Potkat1'}
    });

    tick();
    fixture.detectChanges();

    component.saveOffering();

    tick();
    fixture.detectChanges();

    expect(spyAddOffering).toHaveBeenCalled();
    expect(spyAdd).toHaveBeenCalled();
    expect(spyNavigate).toHaveBeenCalledWith(['/cultural-offering/1']);

  }));

  it('should not save offering because of the invalid form', fakeAsync(() => {
    const photo: CulturalOfferingPhoto = {id: 1, hovering: false, culturalOfferingId: 1};
    const spyAddOffering = spyOn(addOfferingService, 'addOffering').and
      .returnValue(of({id: 1}));
    component.photo = photo;

    component.formGroup.patchValue({
      address: {display_name: 'Adresa'},
      selectedCategory: {id: 1, name: 'Kat1'},
      selectedSubcategory: {id: 1, name: 'Potkat1'}
    });

    tick();
    fixture.detectChanges();

    component.saveOffering();

    tick();
    fixture.detectChanges();

    expect(spyAddOffering).not.toHaveBeenCalled();
    expect(spyAdd).toHaveBeenCalled();
    expect(spyNavigate).not.toHaveBeenCalledWith(['/cultural-offering/1']);

  }));

  it('should not save offering because there is no photo', fakeAsync(() => {
    const spyAddOffering = spyOn(addOfferingService, 'addOffering').and
      .returnValue(of({id: 1}));

    tick();
    fixture.detectChanges();

    component.saveOffering();

    tick();
    fixture.detectChanges();

    expect(spyAddOffering).not.toHaveBeenCalled();
    expect(spyAdd).toHaveBeenCalled();
    expect(spyNavigate).not.toHaveBeenCalledWith(['/cultural-offering/1']);

  }));

  it('should save offering in edit mode', fakeAsync(() => {
    (component as any).activatedRoute.snapshot = {
      data: {mode: 'edit'},
      params: {id: 1}
    };
    component.ngOnInit();
    tick();
    fixture.detectChanges();

    const photo: CulturalOfferingPhoto = {id: 1, hovering: false, culturalOfferingId: 1};
    const spyEditOffering = spyOn(addOfferingService, 'editOffering').and
      .returnValue(of({id: 1}));
    component.photo = photo;

    component.formGroup.patchValue({
      name: 'Ime',
      briefInfo: 'Info',
      address: {display_name: 'Adresa'},
      selectedCategory: {id: 1, name: 'Kat1'},
      selectedSubcategory: {id: 1, name: 'Potkat1'}
    });

    tick();
    fixture.detectChanges();

    component.saveOffering();

    tick();
    fixture.detectChanges();

    expect(spyEditOffering).toHaveBeenCalled();
    expect(spyAdd).toHaveBeenCalled();
    expect(spyNavigate).toHaveBeenCalledWith(['/cultural-offering/1']);
  }));

  it('should get categories from addofferingservice', () => {
    addOfferingService.categories = getCategories();

    expect(component.categories).toEqual(getCategories());
  });

  it('should get subcategories from addofferingservice', () => {
    addOfferingService.subcategories = getSubcategories();

    expect(component.subcategories).toEqual(getSubcategories());
  });

  it('should get thumbanil url for photo', () => {
    const photo: CulturalOfferingPhoto = {id: 1, hovering: false, culturalOfferingId: 1};

    component.photo = photo;
    expect(component.thumbnailPhoto).toEqual('/photos/main/thumbnail/1.png');
  });

  it('should clear photos on destory', () => {
    const spyClearPhotos = spyOn(addOfferingService, 'clearPhotos');
    component.ngOnDestroy();

    expect(spyClearPhotos).toHaveBeenCalled();
  });
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

function getCategories(): Category[] {
  return [
    {id: 1, name: 'cat1', numSubcategories: 2},
    {id: 2, name: 'cat2', numSubcategories: 0}
  ];
}

function getSubcategories(): Subcategory[] {
  return [
    {id: 1, categoryId: 1, name: 'potk1'},
    {id: 2, categoryId: 1, name: 'potk2'}
  ];
}
