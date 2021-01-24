import {ComponentFixture, discardPeriodicTasks, fakeAsync, flush, TestBed, tick} from '@angular/core/testing';

import {MapViewComponent} from './map-view.component';
import {MapService} from '../../core/services/map/map.service';
import {ActivatedRoute, Router} from '@angular/router';
import {AuthService} from '../../core/services/auth/auth.service';
import {BehaviorSubject, of} from 'rxjs';
import {CulturalOfferingMarker} from '../../core/models/culturalOfferingMarker';
import {CulturalOffering} from '../../core/models/cultural-offering';
import {By} from '@angular/platform-browser';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {OfferingsListComponent} from '../offerings-list/offerings-list.component';
import {VirtualScrollerModule} from 'primeng/virtualscroller';
import * as L from 'leaflet';
import createSpy = jasmine.createSpy;

describe('MapViewComponent', () => {
  let component: MapViewComponent;
  let fixture: ComponentFixture<MapViewComponent>;
  let mapService: MapService;
  let router: Router;
  let authService: AuthService;
  let activatedRoute: ActivatedRoute;

  beforeEach(async () => {
    const culturalOfferings = getCulturalOfferings();
    const authServiceMock = {
      getUserRole: createSpy('getUserRole').and.returnValue('MODERATOR')
    };

    const mapServiceMock = {
      zoom: new BehaviorSubject<number>(0),
      removeMarkers: createSpy('removeMarkers'),
      removeOutOfBounds: createSpy('removeOutOfBounds'),
      markers: {},
      clearMarkers: createSpy('clearMarkers'),
      loadMarkers: createSpy('loadMarkers').and
        .returnValue(of(
          [new CulturalOfferingMarker(culturalOfferings[0]),
            new CulturalOfferingMarker(culturalOfferings[1])]))
    };

    const routerMock = {
      navigate: createSpy('navigate')
    };

    const activatedRouteMock = {queryParams: of({lat: 10, lng: 11})};

    await TestBed.configureTestingModule({
      declarations: [ MapViewComponent, OfferingsListComponent ],
      imports: [BrowserAnimationsModule, VirtualScrollerModule],
      providers: [
        {provide: AuthService, useValue: authServiceMock},
        {provide: Router, useValue: routerMock},
        {provide: MapService, useValue: mapServiceMock},
        {provide: ActivatedRoute, useValue: activatedRouteMock}
      ]
    })
    .compileComponents();
  });


  beforeEach(() => {
    mapService = TestBed.inject(MapService);
    authService = TestBed.inject(AuthService);
    router = TestBed.inject(Router);
    activatedRoute = TestBed.inject(ActivatedRoute);
    fixture = TestBed.createComponent(MapViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should init the mapviewcomponent', () => {
    component.ngOnInit();


    expect(component.queryCenter).toBeDefined();
    expect(component.queryCenter.lat).toEqual(10);
    expect(component.queryCenter.lng).toEqual(11);
  });

  it('should do various stuff afterviewinits', fakeAsync(() => {
    // ngafterviewinit was called implicitly in the beforeeach function

    tick();

    fixture.detectChanges();

    expect(component.mapElement).toBeDefined();
    expect(component.mapElement.nativeElement.childElementCount).toBeGreaterThan(0);
    expect(mapService.zoom.getValue()).toEqual(15);
    expect(mapService.loadMarkers).toHaveBeenCalled();
    expect(router.navigate).toHaveBeenCalledWith(['.'], {
      relativeTo: activatedRoute,
      queryParams: {lat: 10, lng: 11},
      replaceUrl: true
    });
  }));

  it('should enable viewofferings and show the element', fakeAsync(() => {
    mapService.zoom.next(15);
    component.onClickViewOfferings();

    tick();
    discardPeriodicTasks();

    fixture.detectChanges();

    expect(component.viewOfferings).toBeTrue();
    expect(fixture.debugElement.query(By.css('.view-offerings div'))).toBeTruthy();
    expect(fixture.debugElement.query(By.css('.view-offering-button'))).not.toBeTruthy();
  }));

  it('should disable viewofferings and show the button', fakeAsync(() => {
    mapService.zoom.next(15);
    component.onClickCollapse();

    tick();
    discardPeriodicTasks();

    fixture.detectChanges();

    expect(component.viewOfferings).toBeFalse();
    expect(fixture.debugElement.query(By.css('.view-offerings div'))).not.toBeTruthy();
    expect(fixture.debugElement.query(By.css('.view-offering-button'))).toBeTruthy();
  }));

  it('should return the user role from authservice', () => {
    expect(component.getUserRole()).toEqual(authService.getUserRole());
  });

  it('should set route query params from parameter', () => {
    component.setRouteQueryParams(new L.LatLng(10, 11));

    expect(router.navigate).toHaveBeenCalledWith(['.'], {
      relativeTo: activatedRoute,
      queryParams: {lat: 10, lng: 11},
      replaceUrl: true
    });
  });

  it('should set route query params from map center', () => {
    component.setRouteQueryParams();

    expect(router.navigate).toHaveBeenCalledWith(['.'], {
      relativeTo: activatedRoute,
      queryParams: {lat: 10, lng: 11},
      replaceUrl: true
    });
  });

  it('should destroy the component, clear markers and the map', fakeAsync(() => {
    component.ngOnDestroy();

    expect(mapService.clearMarkers).toHaveBeenCalled();
    expect(document.body.style.overflow).toEqual('auto');
  }));

  it('should show add button when user is moderator', () => {
    expect(fixture.debugElement.query(By.css('.add-button'))).toBeTruthy();
  });

  it('should not show add button when user is logged in with the role user', fakeAsync(() => {
    // TestBed.overrideProvider(AuthService, {useValue: authServiceMock}); doesn't work !!!!
    (component as any).authService = {
      getUserRole: createSpy('getUserRole').and.returnValue('USER')
    };

    tick();

    fixture.detectChanges();

    expect(fixture.debugElement.query(By.css('.add-button'))).not.toBeTruthy();
  }));

  it('should show the view offerings button when map zoom is higher than or equal to 15', fakeAsync(() => {
    mapService.zoom.next(15);

    tick();
    discardPeriodicTasks();

    fixture.detectChanges();

    expect(fixture.debugElement.query(By.css('.view-offerings'))).toBeTruthy();
  }));

  it('should not show the view offerings button when map zoom is lower than 15', fakeAsync(() => {
    mapService.zoom.next(0);

    tick();
    discardPeriodicTasks();

    fixture.detectChanges();
    expect(fixture.debugElement.query(By.css('.view-offerings'))).not.toBeTruthy();
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
