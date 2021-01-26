import {fakeAsync, getTestBed, TestBed, tick} from '@angular/core/testing';

import {AddOfferingService} from './add-offering.service';
import {HttpClientTestingModule, HttpTestingController} from '@angular/common/http/testing';
import {HttpClient, HttpErrorResponse} from '@angular/common/http';
import {Category} from '../../models/category';
import {Subcategory} from '../../models/subcategory';
import {CulturalOffering} from '../../models/cultural-offering';
import {Moderator} from '../../models/moderator';
import {CulturalOfferingPhoto} from '../../models/culturalOfferingPhoto';

describe('AddOfferingService', () => {
  let service: AddOfferingService;
  let httpMock: HttpTestingController;
  let httpClient: HttpClient;
  let injector;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [AddOfferingService]
    });

    service = TestBed.inject(AddOfferingService);
    injector = getTestBed();
    httpClient = TestBed.inject(HttpClient);
    httpMock = TestBed.inject(HttpTestingController);
  });

  it('should be created', () => {
    expect(service).toBeDefined();
  });

  it('getCategories() should query url and get all categories', fakeAsync(() => {
    let categories: Category[] = [];
    const mockCategory: Category[] = [
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
    req.flush(mockCategory);

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

  it('getSubcategories() should query url and get all subcategories', fakeAsync(() => {
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

  it('clearPhotos() should query url and clear a photos', () => {
    service.clearPhotos();

    const req = httpMock.expectOne(`/api/cultural-offerings/clear-photos`);
    expect(req.request.method).toBe('DELETE');
    req.flush({});
  });

  it('deletePhotos() should query url and delete a photos', () => {
    service.deletePhotos(1).subscribe(res => {
    });

    const req = httpMock.expectOne(`/api/cultural-offering/photo/1`);
    expect(req.request.method).toBe('DELETE');
    req.flush({});
  });

  it('addPhoto()  should query url and add a photo', fakeAsync(() => {
    let newPhoto: File = new File([''], 'photo');

    const mockPost: File = new File(
      [''],
      'photo');

    let addedPhoto: CulturalOfferingPhoto;

    const offeringPhoto: CulturalOfferingPhoto = {
      id: 1, culturalOfferingId: 1, hovering: false
    };
    service.addPhoto(newPhoto).subscribe(data => {
      addedPhoto = data;
    });
    const req = httpMock.expectOne('/api/cultural-offerings/add-photo');
    expect(req.request.method).toBe('POST');
    req.flush(offeringPhoto);

    tick();

    expect(addedPhoto).toBeDefined();
    expect(addedPhoto).toEqual(offeringPhoto);
  }));

  it('addOffering()  should query url and save a culturalOffering', fakeAsync(() => {
    let newCulturalOffering: CulturalOffering = {
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

    service.addOffering().subscribe(data => newCulturalOffering = data);


    const req = httpMock.expectOne('/api/cultural-offerings');
    expect(req.request.method).toBe('POST');
    req.flush(mockCulturalOffering);

    tick();

    expect(newCulturalOffering).toBeDefined();
    expect(newCulturalOffering.id).toEqual(1);
    expect(newCulturalOffering.name).toEqual('CulturalOffering');
    expect(newCulturalOffering.briefInfo).toEqual('CulturalOfferingInfo');
    expect(newCulturalOffering.latitude).toEqual(10);
    expect(newCulturalOffering.longitude).toEqual(5);
    expect(newCulturalOffering.address).toEqual('9336 Civic Center Dr, Beverly Hills, CA 90210, USA');
    expect(newCulturalOffering.overallRating).toEqual(54);
    expect(newCulturalOffering.numReviews).toEqual(0);
    expect(newCulturalOffering.subcategoryId).toEqual(1);
    expect(newCulturalOffering.subcategoryName).toEqual('subcategory');
    expect(newCulturalOffering.numSubscribed).toEqual(0);
    expect(newCulturalOffering.categoryName).toEqual('Category1');
    expect(newCulturalOffering.categoryId).toEqual(1);
  }));

  it('editOffering()  should query url and save a culturalOffering', fakeAsync(() => {
    let updateCulturalOffering: CulturalOffering = {
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

    service.editOffering().subscribe(data => updateCulturalOffering = data);


    const req = httpMock.expectOne('/api/cultural-offerings');
    expect(req.request.method).toBe('PUT');
    req.flush(mockCulturalOffering);

    tick();

    expect(updateCulturalOffering).toBeDefined();
    expect(updateCulturalOffering.id).toEqual(1);
    expect(updateCulturalOffering.name).toEqual('CulturalOffering');
    expect(updateCulturalOffering.briefInfo).toEqual('CulturalOfferingInfo');
    expect(updateCulturalOffering.latitude).toEqual(10);
    expect(updateCulturalOffering.longitude).toEqual(5);
    expect(updateCulturalOffering.address).toEqual('9336 Civic Center Dr, Beverly Hills, CA 90210, USA');
    expect(updateCulturalOffering.overallRating).toEqual(54);
    expect(updateCulturalOffering.numReviews).toEqual(0);
    expect(updateCulturalOffering.subcategoryId).toEqual(1);
    expect(updateCulturalOffering.subcategoryName).toEqual('subcategory');
    expect(updateCulturalOffering.numSubscribed).toEqual(0);
    expect(updateCulturalOffering.categoryName).toEqual('Category1');
    expect(updateCulturalOffering.categoryId).toEqual(1);
  }));

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

    service.getOffering(1).subscribe(data => culturalOffering = data);


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
    service.getOffering(1).subscribe(null, e => {
      error = e;
    });
    const req = httpMock.expectOne('/api/cultural-offerings/1');
    expect(req.request.method).toBe('GET');

    req.flush('Something went wrong', {
      status: 404,
      statusText: 'Not Found'
    });

    expect(error.statusText).toEqual('Not Found');
    expect(error.status).toEqual(404);
  });
});
