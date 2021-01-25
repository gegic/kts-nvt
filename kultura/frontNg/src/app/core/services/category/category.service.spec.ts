import {fakeAsync, getTestBed, TestBed, tick} from '@angular/core/testing';

import {CategoryService} from './category.service';
import {HttpClientTestingModule, HttpTestingController} from '@angular/common/http/testing';
import {HttpClient, HttpErrorResponse} from '@angular/common/http';
import {Category} from '../../models/category';
import {Subcategory} from '../../models/subcategory';
import {Moderator} from '../../models/moderator';

describe('CategoryService', () => {
  let injector;
  let service: CategoryService;
  let httpMock: HttpTestingController;
  let httpClient: HttpClient;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [CategoryService]
    });

    injector = getTestBed();
    httpClient = TestBed.inject(HttpClient);
    httpMock = TestBed.inject(HttpTestingController);
    service = TestBed.inject(CategoryService);
  });

  it('should be created', () => {
    expect(service).toBeDefined();
  });

  it('getCategories() should query url and get all category', fakeAsync(() => {
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

  it('create()  should query url and save a category', fakeAsync(() => {
    let newCategory: Category = {
      name: 'Category1',
      numSubcategories: 1
    };

    const mockCategory: Category =
      {
        id: 1,
        name: 'Category1',
        numSubcategories: 1
      };

    service.create(newCategory).subscribe(data => newCategory = data);


    const req = httpMock.expectOne('/api/categories');
    expect(req.request.method).toBe('POST');
    req.flush(mockCategory);

    tick();

    expect(newCategory).toBeDefined();
    expect(newCategory.id).toEqual(1);
    expect(newCategory.name).toEqual('Category1');
    expect(newCategory.numSubcategories).toEqual(1);
  }));

  it('update() should query url and edit a category', fakeAsync(() => {
    let updateCategory: Category = {
      name: 'Category1',
      numSubcategories: 1
    };

    const mockCategory: Category =
      {
        id: 1,
        name: 'CategoryUpdate',
        numSubcategories: 1
      };

    service.update(updateCategory).subscribe(res => updateCategory = res
    );

    const req = httpMock.expectOne('/api/categories');
    expect(req.request.method).toBe('PUT');
    req.flush(mockCategory);

    tick();
    expect(updateCategory).toBeDefined();
    expect(updateCategory.id).toEqual(1);
    expect(updateCategory.name).toEqual('CategoryUpdate');
    expect(updateCategory.numSubcategories).toEqual(1);
  }));

  it('delete() should query url and delete a category', () => {
    service.delete(1).subscribe(res => {
    });

    const req = httpMock.expectOne(`/api/categories/1`);
    expect(req.request.method).toBe('DELETE');
    req.flush({});
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

  it('updateSubcategory() should query url and edit a subcategory', fakeAsync(() => {
    let updateSubcategory: Subcategory = {
      id: 1,
      name: 'Subcategory',
      categoryId: 1,
      numOfferings: 0
    };

    const mockSubcategory: Subcategory =
      {
        id: 1,
        name: 'SubcategoryUpdated',
        categoryId: 1,
        numOfferings: 0
      };

    service.updateSubcategory(updateSubcategory).subscribe(res => updateSubcategory = res
    );

    const req = httpMock.expectOne('/api/subcategories');
    expect(req.request.method).toBe('PUT');
    req.flush(mockSubcategory);

    tick();
    expect(updateSubcategory).toBeDefined();
    expect(updateSubcategory.id).toEqual(1);
    expect(updateSubcategory.name).toEqual('SubcategoryUpdated');
    expect(updateSubcategory.categoryId).toEqual(1);
    expect(updateSubcategory.numOfferings).toEqual(0);
  }));

  it('createSubcategory() should query url and add a subcategory', fakeAsync(() => {
    let newSubcategory: Subcategory = {
      name: 'Subcategory',
      categoryId: 1,
      numOfferings: 0
    };

    const mockSubcategory: Subcategory =
      {
        id: 1,
        name: 'Subcategory',
        categoryId: 1,
        numOfferings: 0
      };

    service.createSubcategory(newSubcategory).subscribe(res => newSubcategory = res
    );

    const req = httpMock.expectOne('/api/subcategories');
    expect(req.request.method).toBe('POST');
    req.flush(mockSubcategory);

    tick();
    expect(newSubcategory).toBeDefined();
    expect(newSubcategory.id).toEqual(1);
    expect(newSubcategory.name).toEqual('Subcategory');
    expect(newSubcategory.categoryId).toEqual(1);
    expect(newSubcategory.numOfferings).toEqual(0);
  }));

  it('delete() should query url and delete a subcategory', () => {
    service.deleteSubcategory(1).subscribe(res => {
    });

    const req = httpMock.expectOne(`/api/subcategories/1`);
    expect(req.request.method).toBe('DELETE');
    req.flush({});
  });

  it('should throw error', () => {
    let error: HttpErrorResponse;
    const mockSubcategory: Subcategory =
      {
        id: 1,
        name: 'SubcategoryUpdated',
        categoryId: 1,
        numOfferings: 0
      };
    service.create(mockSubcategory).subscribe(null, e => {
      error = e;
    });
    const req = httpMock.expectOne('/api/categories');
    expect(req.request.method).toBe('POST');

    req.flush('Something went wrong', {
      status: 404,
      statusText: 'Network error'
    });

    expect(error.statusText).toEqual('Network error');
    expect(error.status).toEqual(404);
  });
});
