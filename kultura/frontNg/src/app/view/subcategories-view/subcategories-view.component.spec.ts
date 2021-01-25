import {ComponentFixture, fakeAsync, TestBed, tick} from '@angular/core/testing';

import { SubcategoriesViewComponent } from './subcategories-view.component';
import {DynamicDialogConfig, DynamicDialogModule, DynamicDialogRef} from 'primeng/dynamicdialog';
import {TableModule} from 'primeng/table';
import {Category} from '../../core/models/category';
import {of} from 'rxjs';
import {CategoryService} from '../../core/services/category/category.service';
import {ConfirmationService, MessageService} from 'primeng/api';
import {HttpClientModule} from '@angular/common/http';
import {DialogModule} from 'primeng/dialog';
import {ToolbarModule} from 'primeng/toolbar';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import Spy = jasmine.Spy;
import {By} from '@angular/platform-browser';

describe('SubcategoriesViewComponent', () => {
  let component: SubcategoriesViewComponent;
  let fixture: ComponentFixture<SubcategoriesViewComponent>;
  let dynamicDialogRef: any;
  let dynamicDialogConfig: any;
  let categoryService: any;
  let messageService: any;
  let confirmationService: any;
  let category: Category;
  beforeEach( () => {
    category = {
      id: 71,
      name: 'kategooorija',
      numSubcategories: 3
    };
    const subcategories = {
      content: [
        {
          id: 1,
          name: 'Subcategory1',
          numOfferings: 0,
          categoryId: 71
        },
        {
          id: 2,
          name: 'Subcategory2',
          categoryId: 71,
          numOfferings: 1
        },
        {
          id: 3,
          name: 'Subcategory3',
          categoryId: 71,
          numOfferings: 2
        },
      ],
      totalElements: 3
    };
    const categoryServiceMock = {
      getSubcategories: jasmine.createSpy('getSubcategories').and.returnValue(of(subcategories))
    };
    const messageServiceMock = {
      add: jasmine.createSpy('add').and.returnValue(of({}))
    };
    const confirmationServiceMock = {
      confirm: jasmine.createSpy('confirm').and.returnValue(of())
    };
    const configMock = {
      data: {category}
    };
    TestBed.configureTestingModule({
      declarations: [ SubcategoriesViewComponent ],
      imports: [DynamicDialogModule, TableModule, HttpClientModule, DialogModule,
        ToolbarModule, ReactiveFormsModule, FormsModule],
      providers: [
        DynamicDialogRef,
        { provide: DynamicDialogConfig, useValue: configMock },
        { provide: MessageService, useValue: messageServiceMock },
        { provide: ConfirmationService, useValue: confirmationServiceMock },
        { provide: CategoryService, useValue: categoryServiceMock },
      ]
    })
      .compileComponents();
    fixture = TestBed.createComponent(SubcategoriesViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    categoryService = TestBed.inject(CategoryService);
    messageService = TestBed.inject(MessageService);
    confirmationService = TestBed.inject(ConfirmationService);
    dynamicDialogRef = TestBed.inject(DynamicDialogRef);
    dynamicDialogConfig = TestBed.inject(DynamicDialogConfig);
  });


  it('should create', () => {
    expect(component).toBeTruthy();
  });
  it('should load category on init', fakeAsync(() => {
    component.ngOnInit();
    fixture.detectChanges();
    tick();
    expect(component.category).toEqual(category);
    // expect(dynamicDialogConfig.data.category).toEqual(category);
  }));
  it('should render table', fakeAsync (() => {
    component.ngOnInit();
    fixture.detectChanges();
    tick();

    // const result = fixture.debugElement.query(By.css('.subcategoriesTable')).nativeElement;
    fixture.detectChanges();
    tick();
    const tableRows = fixture.nativeElement.querySelectorAll('tr');
    expect(tableRows.length).toBe(4);
  }));
});
