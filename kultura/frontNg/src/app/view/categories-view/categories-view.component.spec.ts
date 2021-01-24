import {async, ComponentFixture, fakeAsync, TestBed, tick} from '@angular/core/testing';

import { CategoriesViewComponent } from './categories-view.component';
import {CategoryService} from '../../core/services/category/category.service';
import {ConfirmationService, MessageService} from 'primeng/api';
import {DialogService} from 'primeng/dynamicdialog';
import {Category} from '../../core/models/category';
import {Observable, of} from 'rxjs';
import {DebugElement} from '@angular/core';
import {By} from '@angular/platform-browser';
import {CategoryListItemComponent} from '../category-list-item/category-list-item.component';
import {DialogModule} from 'primeng/dialog';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {CardModule} from 'primeng/card';

describe('CategoriesViewComponent', () => {
  let component: CategoriesViewComponent;
  let fixture: ComponentFixture<CategoriesViewComponent>;
  let categoryService: CategoryService;
  let messageService: MessageService;
  let dialogService: DialogService;
  const category = {
    id: 1,
    name: 'Category1',
    numSubcategories: 1
  };

  beforeEach(() => {
    // tslint:disable-next-line:one-variable-per-declaration
    const mockCategory = {
      content: [
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
      },
      ],
      totalPages: 1,
      pageable : {
        pageNumber: 1
      }
  };

    const categoryServiceMocked = {
      getCategories: jasmine.createSpy('getCategories').and.returnValue(of(mockCategory)),
      categories: jasmine.createSpy('categories').and.returnValue(of(mockCategory)),
      create: jasmine.createSpy('create').and.returnValue(of({}))
    };
    const messageServiceMocked = {
      add: jasmine.createSpy('add').and.returnValue(of({}))
    };
    const dialogServiceMocked = {
      open: jasmine.createSpy('open').and.returnValue(of({}))
    };
    TestBed.configureTestingModule({
      declarations: [ CategoriesViewComponent, CategoryListItemComponent ],
      providers:    [ {provide: CategoryService, useValue: categoryServiceMocked },
        { provide: MessageService, useValue: messageServiceMocked },
        { provide: DialogService, useValue: dialogServiceMocked},
        {provide: ConfirmationService, useValue: ConfirmationService}],
      imports: [DialogModule, ReactiveFormsModule, FormsModule, CardModule]
    })
      .compileComponents();
    fixture = TestBed.createComponent(CategoriesViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    categoryService = TestBed.inject(CategoryService);
    messageService = TestBed.inject(MessageService);
    dialogService = TestBed.inject(DialogService);
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should fetch the categories list on init', fakeAsync (() => {
    component.ngOnInit();
    expect(categoryService.getCategories).toHaveBeenCalled();

    tick();

    fixture.whenStable()
      .then(() => {
        expect(component.categories.length).toBe(3);
        fixture.detectChanges();
        const elements: DebugElement[] =
          fixture.debugElement.queryAll(By.css('h2'));
        expect(elements.length).toBe(4); // header h2 plus one tr for each category
      });
  }));

  it('should open Add dialog when clicked', fakeAsync( () => {
    component.openAddDialog(true, category);
    expect(component.isAddDialogOpen).toBeTrue();
    expect(component.editingCategory.name).toEqual('Category1');
  }));

  it('should open Subcategories dialog when clicked', fakeAsync( () => {
    component.openSubcategoriesDialog(category);
    expect(dialogService.open).toHaveBeenCalled();
  }));

  it('hide dialog removes dialog and category info from it', fakeAsync( () => {
    component.onHideAddDialog();
    expect(component.editingCategory).toBeUndefined();
  }));

  it('adds new category', fakeAsync( () => {
    component.ngOnInit();
    fixture.detectChanges();  // ngOnInit will be called
    component.openAddDialog();
    component.nameControl.setValue('TestCategory42');
    tick();

    component.saveCategory();

    tick();
    const cat = new Category();
    cat.name = 'TestCategory42';
    expect(categoryService.create).toHaveBeenCalledWith(cat);
  }));

  // a helper function to tell Angular that an event on the HTML page has happened
  // tslint:disable-next-line:typedef
  function newEvent(eventName: string, bubbles = false, cancelable = false) {
    const evt = document.createEvent('CustomEvent');  // MUST be 'CustomEvent'
    evt.initCustomEvent(eventName, bubbles, cancelable, null);
    return evt;
  }
});
