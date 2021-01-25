import {fakeAsync, getTestBed, TestBed, tick} from '@angular/core/testing';

import {CulturalOfferingDetailsService} from './cultural-offering-details.service';
import {HttpClientTestingModule, HttpTestingController} from '@angular/common/http/testing';
import {HttpClient, HttpErrorResponse} from '@angular/common/http';
import {CulturalOffering} from '../../models/cultural-offering';
import {Subcategory} from '../../models/subcategory';

describe('CulturalOfferingDetailsService', () => {
  let service: CulturalOfferingDetailsService;
  let httpMock: HttpTestingController;
  let httpClient: HttpClient;
  let injector;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [CulturalOfferingDetailsService]
    });
    service = TestBed.inject(CulturalOfferingDetailsService);
    injector = getTestBed();
    httpClient = TestBed.inject(HttpClient);
    httpMock = TestBed.inject(HttpTestingController);
  });

  it('should be created', () => {
    expect(service).toBeDefined();
  });

  it('getOffering()  should query url and save a culturalOffering', fakeAsync(() => {
    let culturalOffering: CulturalOffering = {
      id: 0,
      name: '',
      briefInfo: '',
      latitude: 0,
      longitude: 0,
      address: '',
      overallRating: 0,
      numReviews: 0,
      subcategoryId: 0,
      subcategoryName: '',
      numSubscribed: 0,
      categoryName: '',
      categoryId: 0,
    };

    const mockCulturalOffering: CulturalOffering =
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

    service.getCulturalOffering(1).subscribe(data => culturalOffering = data);


    const req = httpMock.expectOne('/api/cultural-offerings/1');
    expect(req.request.method).toBe('GET');
    req.flush(mockCulturalOffering);

    tick();

    expect(culturalOffering).toBeDefined();
    expect(culturalOffering.id).toEqual(1);
    expect(culturalOffering.name).toEqual('CulturalOffering');
    expect(culturalOffering.briefInfo).toEqual('CulturalOfferingInfo');
    expect(culturalOffering.latitude).toEqual(10);
    expect(culturalOffering.longitude).toEqual(5);
    expect(culturalOffering.address).toEqual('9336 Civic Center Dr, Beverly Hills, CA 90210, USA');
    expect(culturalOffering.overallRating).toEqual(54);
    expect(culturalOffering.numReviews).toEqual(0);
    expect(culturalOffering.subcategoryId).toEqual(1);
    expect(culturalOffering.subcategoryName).toEqual('subcategory');
    expect(culturalOffering.numSubscribed).toEqual(0);
    expect(culturalOffering.categoryName).toEqual('Category1');
    expect(culturalOffering.categoryId).toEqual(1);
  }));

  it('should throw error', () => {
    let error: HttpErrorResponse;

    service.getCulturalOffering(1, 1).subscribe(null, e => {
      error = e;
    });
    const req = httpMock.expectOne('/api/cultural-offerings/1?user=1');
    expect(req.request.method).toBe('GET');

    req.flush('Something went wrong', {
      status: 404,
      statusText: 'Network error'
    });

    expect(error.statusText).toEqual('Network error');
    expect(error.status).toEqual(404);
  });
});
