import {ComponentFixture, fakeAsync, TestBed, tick} from '@angular/core/testing';

import { CategoryListItemComponent } from './category-list-item.component';
import {CategoryService} from '../../core/services/category/category.service';
import {Category} from '../../core/models/category';
import {By} from '@angular/platform-browser';
import {ConfirmationService, MessageService} from 'primeng/api';
import {CardModule} from 'primeng/card';
import {of} from 'rxjs';
import {EventEmitter} from '@angular/core';

describe('CategoryListItemComponent', () => {
  let component: CategoryListItemComponent;
  let fixture: ComponentFixture<CategoryListItemComponent>;
  let categoryService: any;
  let messageService: any;
  let confirmationService: any;
  let category: Category;

  beforeEach( () => {
    const categoryServiceMock = {
      delete: jasmine.createSpy('delete').and.returnValue(of())
    };
    const messageServiceMock = {
      add: jasmine.createSpy('add').and.returnValue(of({}))
    };
    const confirmationServiceMock = {
      confirm: jasmine.createSpy('confirm').and.returnValue(of())
    };

    TestBed.configureTestingModule({
      imports: [CardModule],
      declarations: [ CategoryListItemComponent ],
      providers:  [ {provide: CategoryService, useValue: categoryServiceMock },
        { provide: MessageService, useValue: messageServiceMock },
        { provide: ConfirmationService, useValue: confirmationServiceMock}]
    })
    .compileComponents();
    fixture = TestBed.createComponent(CategoryListItemComponent);
    component = fixture.componentInstance;


    category = {
      id: 71,
      name: 'kategooorija'
    };

    component.category = category;
    component.clickSubcategories = new EventEmitter<Category>();
    component.categoryDeleted = new EventEmitter<any>();
    fixture.detectChanges();
    categoryService = TestBed.inject(CategoryService);
    messageService = TestBed.inject(MessageService);
    confirmationService = TestBed.inject(ConfirmationService);
  });


  it('should create', () => {
    expect(component).toBeTruthy();
  });
  it('should fetch category on init and show it', fakeAsync(() => {
    fixture.detectChanges();  // ngOnInit will be called
    tick();
    fixture.whenStable().then(() => {
      let categoryName = fixture.debugElement.query(By.css('#pkategooorija')).nativeElement.innerHTML;
      categoryName = categoryName.trim();
      expect(categoryName).toBe('kategooorija');
    });
  }));
  it('should call onClickDelete', fakeAsync(() => {
    component.onClickDelete();
    tick();
    expect(confirmationService.confirm).toHaveBeenCalled();
  }));
  it('should call deletionConfirmed and delete the category', fakeAsync(() => {
    fixture.detectChanges();
    component.deletionConfirmed();
    tick();
    expect(categoryService.delete).toHaveBeenCalledWith(category.id);
  }));
  it('should call onClickSubcategories and emit event', fakeAsync(() => {
    fixture.detectChanges();
    spyOn(component.clickSubcategories, 'emit');
    const subcategoriesButton = fixture.debugElement
      .query(By.css('#subcategorieskategooorija')).nativeElement;
    subcategoriesButton.dispatchEvent(new Event('click'));
    fixture.detectChanges();
    tick();
    expect(component.clickSubcategories.emit).toHaveBeenCalledWith(category);
  }));
  it('should call onClickEdit and emit event', fakeAsync(() => {
    fixture.detectChanges();
    spyOn(component.clickEdit, 'emit');
    const subcategoriesButton = fixture.debugElement
      .query(By.css('#editkategooorija')).nativeElement;
    subcategoriesButton.dispatchEvent(new Event('click'));
    fixture.detectChanges();
    tick();
    expect(component.clickEdit.emit).toHaveBeenCalledWith(category);
  }));
});
