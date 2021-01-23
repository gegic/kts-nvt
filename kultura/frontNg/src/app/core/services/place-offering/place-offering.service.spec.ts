import {fakeAsync, getTestBed, TestBed, tick} from '@angular/core/testing';
import {PlaceOfferingService} from './place-offering.service';
import {HttpClientTestingModule, HttpTestingController} from '@angular/common/http/testing';
import {HttpClient} from '@angular/common/http';
import {CulturalOfferingDetailsService} from '../cultural-offering-details/cultural-offering-details.service';
import * as L from 'leaflet';
import {Place} from '../../models/place';

describe('PlaceOfferingService', () => {
  let service: PlaceOfferingService;
  let httpMock: HttpTestingController;
  let httpClient: HttpClient;
  let injector;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [PlaceOfferingService]
    });
    service = TestBed.inject(PlaceOfferingService);
    injector = getTestBed();
    httpClient = TestBed.inject(HttpClient);
    httpMock = TestBed.inject(HttpTestingController);
  });

  it('should be created', () => {
    expect(service).toBeDefined();
  });

  it('should send a request to retrieve address based on coordinates', fakeAsync(() => {
    let address: string;
    const mockAddress = 'Adresa, Pacir, Subotica';
    service.getAddress(L.latLng(10, 11)).subscribe(val => {
      address = val;
    });

    const req = httpMock.expectOne('https://nominatim.openstreetmap.org/reverse?format=jsonv2&lat=10&lon=11');
    expect(req.request.method).toBe('GET');
    req.flush(mockAddress);

    tick();

    expect(address).toEqual(mockAddress);
  }));

  it('should send request to get recommendations', fakeAsync(() => {
    let recommendations = null;
    const mockRecommendations = [
      {display_name: 'Tutin Srbija'}
    ];
    service.getRecommendations('Tutin').subscribe(val => {
      recommendations = val;
    });

    const req = httpMock.expectOne('https://nominatim.openstreetmap.org/search?format=jsonv2&q=Tutin&limit=6');
    expect(req.request.method).toBe('GET');
    req.flush(mockRecommendations);

    tick();

    expect(recommendations.length).toEqual(1);
    expect(recommendations[0].display_name).toEqual('Tutin Srbija');

  }));

  it('should select a recommendation', () => {
    service.recommendationSelected('Adresa', [10, 11]);

    expect(service.place.getValue().address).toEqual('Adresa');
    expect(service.place.getValue().ocean).toBeFalse();

    expect(service.latLng.getValue().lat).toEqual(10);
    expect(service.latLng.getValue().lng).toEqual(11);

    service.latLng.next(null);
    service.place.next(new Place(null, false));
  });

  it('should reset selected address and coordinates', () => {
    service.latLng.next(new L.LatLng(10, 11));
    service.place.next(new Place('Adresa', false));

    service.reset();

    expect(service.place.getValue().address).toEqual(null);
    expect(service.place.getValue().ocean).toBeFalse();

    expect(service.latLng.getValue()).toBeNull();

  });
});
