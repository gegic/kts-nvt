import {ComponentFixture, fakeAsync, flush, TestBed, tick} from '@angular/core/testing';

import { CulturalOfferingPlaceComponent } from './cultural-offering-place.component';
import {PlaceOfferingService} from '../../core/services/place-offering/place-offering.service';
import Spy = jasmine.Spy;
import createSpy = jasmine.createSpy;
import {BehaviorSubject, of} from 'rxjs';
import {Place} from '../../core/models/place';
import * as L from 'leaflet';
import {DynamicDialogRef} from 'primeng/dynamicdialog';
import {MessageService} from 'primeng/api';
import {LatLng} from 'leaflet';
import {By} from '@angular/platform-browser';

describe('CulturalOfferingPlaceComponent', () => {
  let component: CulturalOfferingPlaceComponent;
  let fixture: ComponentFixture<CulturalOfferingPlaceComponent>;
  let placeOfferingService: PlaceOfferingService;
  let dynamicDialogRef: DynamicDialogRef;
  let addSpy: Spy;

  beforeEach(async () => {

    const placeOfferingServiceMock = {
      place: new BehaviorSubject<Place>(new Place(null, false)),
      latLng: new BehaviorSubject<L.LatLng | null>(null),
      getAddress: createSpy('getAddress').and.returnValue(of({display_name: 'Adresa'}))
    };

    const dialogRef = {
      close: createSpy('close')
    };
    await TestBed.configureTestingModule({
      declarations: [ CulturalOfferingPlaceComponent ],
      providers: [
        {provide: PlaceOfferingService, useValue: placeOfferingServiceMock},
        {provide: DynamicDialogRef, useValue: dialogRef},
        MessageService
      ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    placeOfferingService = TestBed.inject(PlaceOfferingService);
    dynamicDialogRef = TestBed.inject(DynamicDialogRef);
    addSpy = spyOn(TestBed.inject(MessageService), 'add');
    fixture = TestBed.createComponent(CulturalOfferingPlaceComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should setup map and not set coordinates', fakeAsync(() => {
    // ngonviewinit called implicitly

    tick();
    fixture.detectChanges();

    expect(component.mapElement).toBeTruthy();
    expect(placeOfferingService.latLng.getValue()).toBeNull();
    expect(placeOfferingService.place.getValue().address).toBeNull();
  }));

  it('should setup map and set coordinates', fakeAsync(() => {
    component.ngOnDestroy();

    tick();

    placeOfferingService.latLng.next(new LatLng(10, 11));

    component.ngAfterViewInit();

    tick();
    flush();
    expect(component.mapElement).toBeTruthy();
    expect(placeOfferingService.latLng.getValue()).not.toBeNull();
    expect(placeOfferingService.place.getValue().address).toEqual('Adresa');
  }));

  it('should remove previous marker and call set coordinates', fakeAsync(() => {
    const latlng = new LatLng(10, 11);
    component.addMarker({latlng});

    tick();
    fixture.detectChanges();

    expect(fixture.debugElement.query(By.css('.leaflet-marker-pane'))).toBeTruthy();
    expect(placeOfferingService.getAddress).toHaveBeenCalledWith(latlng);
    expect(placeOfferingService.place.getValue().address).toEqual('Adresa');
  }));

  it('should set place', fakeAsync(() => {
    placeOfferingService.place.next(new Place('Adresa', false));
    placeOfferingService.latLng.next(new LatLng(10, 11));

    component.onClickPlace();

    tick();
    fixture.detectChanges();

    expect(dynamicDialogRef.close).toHaveBeenCalledWith({
      address: 'Adresa',
      coordinates: [10, 11]
    });
  }));

  it('should get address from placeOfferingService', () => {
    placeOfferingService.place.next(new Place('Adresa', false));
    expect(component.address).toEqual('Adresa');
  });

  it('should get quick address from placeOfferingService', () => {
    placeOfferingService.place.next(new Place('Adresa', false));
    expect(component.address).toEqual('Adresa');
  });

  it('should get quick address from placeOfferingService when it is null', () => {
    placeOfferingService.place.next(new Place(null, false));
    expect(component.quickAddress).toEqual('Invalid address');
  });

  it('should get coordinates from placeOfferingService', () => {
    const latLng = new LatLng(10, 11);
    placeOfferingService.latLng.next(latLng);
    expect(component.latLng).toEqual(latLng);
  });

  it('should remove map and all its elements', fakeAsync(() => {
    component.ngOnDestroy();

    tick();
    fixture.detectChanges();

    expect(component.mapElement.nativeElement.childElementCount).toEqual(0);
  }));

});
