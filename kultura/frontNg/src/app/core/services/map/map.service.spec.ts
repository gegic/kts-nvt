import {fakeAsync, getTestBed, TestBed, tick} from '@angular/core/testing';

import {MapService} from './map.service';
import {HttpClientTestingModule, HttpTestingController} from '@angular/common/http/testing';
import {AddOfferingService} from '../add-offering/add-offering.service';
import {HttpClient} from '@angular/common/http';
import {CulturalOffering} from '../../models/cultural-offering';
import {CulturalOfferingMarker} from '../../models/culturalOfferingMarker';

describe('MapService', () => {
  let service: MapService;
  let httpMock: HttpTestingController;
  let httpClient: HttpClient;
  let injector;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [MapService]
    });
    service = TestBed.inject(MapService);
    injector = getTestBed();
    httpClient = TestBed.inject(HttpClient);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeDefined();
  });

  it('loadMarkers() should retrieve cultural offerings based on boundaries', fakeAsync(() => {
    let culturalOfferings: CulturalOffering[] = [];

    const mockCulturalOfferings: CulturalOffering[] = getCulturalOfferings();

    service.loadMarkers(10, 16, 14, 15.8).subscribe(val => {
      culturalOfferings = val;
    });
    const req = httpMock.expectOne('/api/cultural-offerings/bounds?lng-start=14&lng-end=15.8' +
      '&lat-start=10&lat-end=16');
    expect(req.request.method).toBe('GET');
    req.flush(mockCulturalOfferings);

    tick();

    // (\w+): (['A-Za-z0-9 ]+),?
    // expect(culturalOfferings[0].$1).toEqual($2);
    expect(culturalOfferings.length).toEqual(2);
    expect(culturalOfferings[0].id).toEqual(1);
    expect(culturalOfferings[0].latitude).toEqual(13);
    expect(culturalOfferings[0].longitude).toEqual(15);
    expect(culturalOfferings[0].name).toEqual('My offering');
    expect(culturalOfferings[0].briefInfo).toEqual('Some info');
    expect(culturalOfferings[0].address).toEqual('Svetozara Markovica');
    expect(culturalOfferings[0].numSubscribed).toEqual(0);
    expect(culturalOfferings[0].subscribed).toEqual(false);
    expect(culturalOfferings[0].subcategoryId).toEqual(1);
    expect(culturalOfferings[0].categoryId).toEqual(1);
    expect(culturalOfferings[0].subcategoryName).toEqual('Vasar');
    expect(culturalOfferings[0].categoryName).toEqual('Manifestacija');
    expect(culturalOfferings[0].overallRating).toEqual(0);
    expect(culturalOfferings[0].numReviews).toEqual(0);
    expect(culturalOfferings[0].numPhotos).toEqual(0);
    expect(culturalOfferings[0].photoId).toEqual(1);

    expect(culturalOfferings[1].id).toEqual(2);
    expect(culturalOfferings[1].latitude).toEqual(13.4);
    expect(culturalOfferings[1].longitude).toEqual(15.1);
    expect(culturalOfferings[1].name).toEqual('My second offering');
    expect(culturalOfferings[1].briefInfo).toEqual('Some info');
    expect(culturalOfferings[1].address).toEqual('Svetozara Miletica');
    expect(culturalOfferings[1].numSubscribed).toEqual(1);
    expect(culturalOfferings[1].subscribed).toEqual(false);
    expect(culturalOfferings[1].subcategoryId).toEqual(1);
    expect(culturalOfferings[1].categoryId).toEqual(1);
    expect(culturalOfferings[1].subcategoryName).toEqual('Vasar');
    expect(culturalOfferings[1].categoryName).toEqual('Manifestacija');
    expect(culturalOfferings[1].overallRating).toEqual(0);
    expect(culturalOfferings[1].numReviews).toEqual(0);
    expect(culturalOfferings[1].numPhotos).toEqual(0);
    expect(culturalOfferings[1].photoId).toEqual(2);
  }));

  it('should hide markers', () => {
    const cos = getCulturalOfferings();
    service.markers = {
      1: new CulturalOfferingMarker(cos[0]),
      2: new CulturalOfferingMarker(cos[1])
    };

    service.removeMarkers();
    Object.keys(service.markers).forEach(key => {
      expect(service.markers[key].isVisible()).toBeFalse();
    });
  });

  it('should clear markers', () => {
    const cos = getCulturalOfferings();
    service.markers = {
      1: new CulturalOfferingMarker(cos[0]),
      2: new CulturalOfferingMarker(cos[1])
    };

    service.clearMarkers();
    expect(Object.keys(service.markers).length).toEqual(0);
  });

  it('should remove markers that are out of bounds', () => {
    const cos = getCulturalOfferings();
    service.markers = {
      1: new CulturalOfferingMarker(cos[0]),
      2: new CulturalOfferingMarker(cos[1])
    };


    service.removeOutOfBounds(13.1, 13.6, 14, 16);

    Object.keys(service.markers).forEach(key => {
      const marker = service.markers[key];
      if (marker.isVisible()) {
        expect(marker.culturalOffering.latitude).toBeGreaterThanOrEqual(13.1);
        expect(marker.culturalOffering.latitude).toBeLessThanOrEqual(13.6);
        expect(marker.culturalOffering.longitude).toBeGreaterThanOrEqual(14);
        expect(marker.culturalOffering.longitude).toBeLessThanOrEqual(16);
      } else {
        const latitude = marker.culturalOffering.latitude;
        const longitude = marker.culturalOffering.longitude;
        expect( latitude < 13.1 || latitude > 13.6 || longitude < 14 || longitude > 16).toBeTrue();
      }
    });
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
