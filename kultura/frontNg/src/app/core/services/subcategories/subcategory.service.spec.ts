import {fakeAsync, getTestBed, TestBed, tick} from '@angular/core/testing';

import {SubcategoryService} from './subcategory.service';
import {HttpClientTestingModule, HttpTestingController} from '@angular/common/http/testing';
import {HttpClient, HttpErrorResponse} from '@angular/common/http';
import {CategoryService} from '../category/category.service';
import {Subcategory} from '../../models/subcategory';

describe('SubcategoryService', () => {
  let injector;
  let service: SubcategoryService;
  let httpMock: HttpTestingController;
  let httpClient: HttpClient;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [CategoryService]
    });

    service = TestBed.inject(SubcategoryService);
    injector = getTestBed();
    httpClient = TestBed.inject(HttpClient);
    httpMock = TestBed.inject(HttpTestingController);
  });

  it('should be created', () => {
    expect(service).toBeDefined();
  });

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

    service.getSubcategories(1).subscribe(data => {
      subcategories = data;
    });
    const req = httpMock.expectOne('/api/subcategories/category/1');
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

  it('delete() should query url and delete a subcategory', () => {
    service.delete(1).subscribe(res => {
    });
    const req = httpMock.expectOne(`/api/subcategories/1`);
    expect(req.request.method).toBe('DELETE');
    req.flush({});
  });

  it('should throw error', () => {
    let error: HttpErrorResponse;

    service.getSubcategories(1).subscribe(null, e => {
      error = e;
    });
    const req = httpMock.expectOne('/api/subcategories/category/1');
    expect(req.request.method).toBe('GET');

    req.flush('Something went wrong', {
      status: 404,
      statusText: 'Not Found'
    });

    expect(error.statusText).toEqual('Not Found');
    expect(error.status).toEqual(404);
  });
});
