import {fakeAsync, getTestBed, TestBed, tick} from '@angular/core/testing';

import {CulturalOfferingsService} from './cultural-offerings.service';
import {HttpClientTestingModule, HttpTestingController} from '@angular/common/http/testing';
import {HttpClient} from '@angular/common/http';
import {CulturalOffering} from '../../models/cultural-offering';
import {MapService} from '../map/map.service';
import {Category} from '../../models/category';
import {Subcategory} from '../../models/subcategory';

describe('CulturalOfferingsService', () => {
  let service: CulturalOfferingsService;
  let httpMock: HttpTestingController;
  let httpClient: HttpClient;
  let injector: TestBed;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [CulturalOfferingsService]
    });
    service = TestBed.inject(CulturalOfferingsService);
    injector = getTestBed();
    httpClient = TestBed.inject(HttpClient);
    httpMock = TestBed.inject(HttpTestingController);
  });

  it('should be created', () => {
    expect(service).toBeDefined();
  });

  it('should find all offerings page 1, sorted by id, asc', fakeAsync(() => {

    let culturalOfferings: CulturalOffering[] = [];

    const mockCulturalOfferings: CulturalOffering[] = getCulturalOfferings();

    service.getCulturalOfferings(1, 'id,asc').subscribe(val => {
      culturalOfferings = val;
    });

    const req = httpMock.expectOne('/api/cultural-offerings?page=1&sort=id,asc&no-reviews=true&search=' +
      '&rating-min=1&rating-max=5');
    expect(req.request.method).toBe('GET');
    req.flush(mockCulturalOfferings);

    tick();

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

  it('should find all offerings page 1, sorted by id, asc; query "offering"', fakeAsync(() => {

    let culturalOfferings: CulturalOffering[] = [];

    const mockCulturalOfferings: CulturalOffering[] = getCulturalOfferings();

    service.searchQuery.next('offering');

    service.getCulturalOfferings(1, 'id,asc').subscribe(val => {
      culturalOfferings = val;
    });

    const req = httpMock.expectOne('/api/cultural-offerings?page=1&sort=id,asc&no-reviews=true&search=offering' +
      '&rating-min=1&rating-max=5');
    expect(req.request.method).toBe('GET');
    req.flush(mockCulturalOfferings);

    tick();

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

    service.searchQuery.next('');
  }));

  it('should send request to get recommendations', fakeAsync(() => {
    let recommendations = null;
    const mockRecommendations = [
      {display_name: 'Tutin Srbija'}
    ];
    service.getRecommendations('Tutin').subscribe(val => {
      recommendations = val;
    });

    const req = httpMock.expectOne('https://nominatim.openstreetmap.org/search?format=jsonv2&q=Tutin&limit=4');
    expect(req.request.method).toBe('GET');
    req.flush(mockRecommendations);

    tick();

    expect(recommendations.length).toEqual(1);
    expect(recommendations[0].display_name).toEqual('Tutin Srbija');

  }));

  it('should get relative address for coordinates', fakeAsync(() => {
    let relativeAddress = null;

    service.getRelativeAddress([14, 12]).subscribe(val => {
      relativeAddress = val;
    });

    const req = httpMock.expectOne('https://nominatim.openstreetmap.org/reverse?format=jsonv2&lat=14&lon=12');
    expect(req.request.method).toBe('GET');
    req.flush('Neka adresa nedje u Tutin mozda. Nije sigurno, 36320, Srbija');

    tick();

    expect(relativeAddress).toEqual('Neka adresa nedje u Tutin mozda. Nije sigurno, 36320, Srbija');

  }));

  it('should subscribe a user to a cultural offering', fakeAsync(() => {
    let culturalOffering = null;

    service.subscribe(1, 1).subscribe(val => {
      culturalOffering = val;
    });

    const req = httpMock.expectOne('/api/cultural-offerings/subscribe/cultural-offering/1/user/1');
    expect(req.request.method).toBe('GET');
    req.flush(getCulturalOfferings()[0]);

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

  it('should unsubscribe a user from a cultural offering', fakeAsync(() => {
    let culturalOffering = null;

    service.unsubscribe(1, 1).subscribe(val => {
      culturalOffering = val;
    });

    const req = httpMock.expectOne('/api/cultural-offerings/unsubscribe/cultural-offering/1/user/1');
    expect(req.request.method).toBe('GET');
    req.flush(getCulturalOfferings()[0]);

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

  it('should send request to get recommendations', fakeAsync(() => {
    let categories: Category[] = [];
    const mockCategories: Category[] = [
      {
        id: 1,
        name: 'Category1',
        numSubcategories: 1
      },
      {
        id: 2,
        name: 'Category2',
        numSubcategories: 1
      },
      {
        id: 3,
        name: 'Category3',
        numSubcategories: 1
      }];

    service.getCategories(1).subscribe(data => {
      categories = data;
    });
    const req = httpMock.expectOne('/api/categories?page=1');
    expect(req.request.method).toBe('GET');
    req.flush(mockCategories);

    tick();

    expect(categories.length).toEqual(3, 'should contain given amount of categories');

    expect(categories[0].id).toEqual(1);
    expect(categories[0].name).toEqual('Category1');
    expect(categories[0].numSubcategories).toEqual(1);

    expect(categories[1].id).toEqual(2);
    expect(categories[1].name).toEqual('Category2');
    expect(categories[1].numSubcategories).toEqual(1);

    expect(categories[2].id).toEqual(3);
    expect(categories[2].name).toEqual('Category3');
    expect(categories[2].numSubcategories).toEqual(1);
  }));

  it('getSubcategories() should query url and get subcategories', fakeAsync(() => {
    let subcategories: Subcategory[] = [];
    const mockSubcategory: Subcategory[] = [
      {
        id: 1,
        name: 'Category1',
        categoryId: 1,
        numOfferings: 0
      },
      {
        id: 2,
        name: 'Category2',
        categoryId: 1,
        numOfferings: 0
      },
      {
        id: 3,
        name: 'Category3',
        categoryId: 1,
        numOfferings: 0
      }];

    service.getSubcategories(1, 1).subscribe(data => {
      subcategories = data;
    });
    const req = httpMock.expectOne('/api/subcategories/category/1?page=1');
    expect(req.request.method).toBe('GET');
    req.flush(mockSubcategory);

    tick();

    expect(subcategories.length).toEqual(3, 'should contain given amount of subcategories');

    expect(subcategories[0].id).toEqual(1);
    expect(subcategories[0].name).toEqual('Category1');
    expect(subcategories[0].categoryId).toEqual(1);
    expect(subcategories[0].numOfferings).toEqual(0);

    expect(subcategories[1].id).toEqual(2);
    expect(subcategories[1].name).toEqual('Category2');
    expect(subcategories[1].categoryId).toEqual(1);
    expect(subcategories[1].numOfferings).toEqual(0);

    expect(subcategories[2].id).toEqual(3);
    expect(subcategories[2].name).toEqual('Category3');
    expect(subcategories[2].categoryId).toEqual(1);
    expect(subcategories[2].numOfferings).toEqual(0);
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
