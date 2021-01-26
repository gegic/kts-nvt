import {ComponentFixture, fakeAsync, TestBed, tick} from '@angular/core/testing';

import {SubcategoriesViewComponent} from './subcategories-view.component';
import {DynamicDialogConfig, DynamicDialogModule, DynamicDialogRef} from 'primeng/dynamicdialog';
import {CategoryService} from '../../core/services/category/category.service';
import Spy = jasmine.Spy;
import {DialogModule} from 'primeng/dialog';
import {ConfirmationService, MessageService} from 'primeng/api';
import {Category} from '../../core/models/category';
import createSpy = jasmine.createSpy;
import {Subcategory} from '../../core/models/subcategory';
import {of, throwError} from 'rxjs';
import {Table, TableModule} from 'primeng/table';
import {concatMap} from 'rxjs/operators';
import {ToolbarModule} from 'primeng/toolbar';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';

describe('SubcategoriesViewComponent', () => {
  let component: SubcategoriesViewComponent;
  let fixture: ComponentFixture<SubcategoriesViewComponent>;
  let category: Category;
  let categoryService: CategoryService;
  let confirmationService: ConfirmationService;
  let spyAdd: Spy;

  beforeEach(async () => {
    const subcategories = getSubcategories();
    const mockCategoryService = {
      getSubcategories: createSpy('getSubcategories').and.returnValue(of(subcategories)),
      deleteSubcategory: createSpy('deleteSubcategory').and.returnValue(of()),
      updateSubcategory: createSpy('updateSubcategory').and.returnValue(of()),
      createSubcategory: createSpy('createSubcategory').and.returnValue(of())
    };

    category = {
      id: 1,
      name: 'Kategorija'
    };
    const mockConfig = {
      data: {category}
    };
    await TestBed.configureTestingModule({
      imports: [
        DialogModule,
        TableModule,
        ToolbarModule,
        FormsModule,
        ReactiveFormsModule
      ],
      declarations: [ SubcategoriesViewComponent ],
      providers: [
        MessageService,
        ConfirmationService,
        {provide: DynamicDialogRef, useValue: {}},
        {provide: DynamicDialogConfig, useValue: mockConfig},
        {provide: CategoryService, useValue: mockCategoryService}
      ],
    })
    .compileComponents();
  });

  beforeEach(() => {
    categoryService = TestBed.inject(CategoryService);
    spyAdd = spyOn(TestBed.inject(MessageService), 'add');
    confirmationService = TestBed.inject(ConfirmationService);
    fixture = TestBed.createComponent(SubcategoriesViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should init category from refconfig', () => {
    expect(component.category).toEqual(category);
  });

  it('should get subcategories', fakeAsync(() => {
    component.getSubcategories({first: 0, rows: 10});

    expect(component.isSubcategoriesLoading).toBeTrue();
    tick();
    expect(categoryService.getSubcategories).toHaveBeenCalledWith(1, 0);
    expect(component.subcategories).toEqual(getSubcategories());
    expect(component.isSubcategoriesLoading).toBeFalse();
  }));
  //
  // it('should open subcategories dialog', () => {
  //   component.openNew();
  //
  //   expect(component.isSubcategoryDialogOpen).toBeTrue();
  // });
  //
  // it('should set subcategory for editing', () => {
  //   component.editSubcategory(getSubcategories()[0]);
  //
  //   expect(component.editingSubcategory).toEqual(getSubcategories()[0]);
  //   expect(component.isSubcategoryDialogOpen).toBeTrue();
  //   expect(component.nameControl.value).toEqual(getSubcategories()[0].name);
  // });
  //
  // it('should set subcategory for deletion', () => {
  //   const spyConfirm = spyOn(confirmationService, 'confirm').and.callFake(confirmation => {
  //     return confirmation.accept();
  //   });
  //   const spyDeletionConfirmed = spyOn(component, 'deletionConfirmed');
  //
  //   component.deleteSubcategory(getSubcategories()[0]);
  //
  //   expect(spyConfirm).toHaveBeenCalled();
  //   expect(spyDeletionConfirmed).toHaveBeenCalledWith(getSubcategories()[0]);
  // });
  //
  // it('should delete subcategory after confirmation', fakeAsync(() => {
  //   const spyResetSubcategories = spyOn(component, 'resetSubcategories');
  //
  //   component.deletionConfirmed(getSubcategories()[0]);
  //
  //   tick();
  //   expect(categoryService.deleteSubcategory).toHaveBeenCalledWith(1);
  //   expect(spyAdd).toHaveBeenCalled();
  //   expect(spyResetSubcategories).toHaveBeenCalled();
  // }));
  //
  // it('should fail deleting subcategory after confirmation', fakeAsync(() => {
  //   const spyResetSubcategories = spyOn(component, 'resetSubcategories');
  //   categoryService.deleteSubcategory = createSpy('deleteSubcategory').and
  //     .returnValue(throwError(null));
  //   component.deletionConfirmed(getSubcategories()[0]);
  //
  //   tick();
  //   expect(categoryService.deleteSubcategory).toHaveBeenCalledWith(1);
  //   expect(spyAdd).toHaveBeenCalled();
  //   expect(spyResetSubcategories).not.toHaveBeenCalled();
  // }));
  //
  // it('should reset table', fakeAsync(() => {
  //   component.resetSubcategories();
  //
  //   tick();
  //   fixture.detectChanges();
  //
  //   expect((component.table as Table).value.length).toEqual(0);
  // }));
  //
  // it('should save a new subcategory', fakeAsync(() => {
  //   component.nameControl.setValue('Potkategorija');
  //   component.saveSubcategory();
  //
  //   tick();
  //   fixture.detectChanges();
  //
  //   expect(categoryService.createSubcategory).toHaveBeenCalledWith(jasmine.objectContaining({name: 'Potkategorija'}))
  // }));
});

function getSubcategories(): Subcategory[] {
  return [
    {id: 1, categoryId: 1, name: 'potk1'},
    {id: 2, categoryId: 1, name: 'potk2'}
  ];
}

