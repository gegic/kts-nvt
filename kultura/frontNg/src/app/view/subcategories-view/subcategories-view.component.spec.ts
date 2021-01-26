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
      getSubcategories: createSpy('getSubcategories').and.returnValue(of({content: subcategories, totalElements: 2})),
      deleteSubcategory: createSpy('deleteSubcategory').and.returnValue(of(null)),
      updateSubcategory: createSpy('updateSubcategory').and.returnValue(of(null)),
      createSubcategory: createSpy('createSubcategory').and.returnValue(of(null))
    };

    category = {
      id: 1,
      name: 'Kategorija',
      numSubcategories: 2
    };
    const mockConfig = {
      data: {category}
    };
    await TestBed.configureTestingModule({
      imports: [
        DialogModule,
        ToolbarModule,
        FormsModule,
        ReactiveFormsModule,
        TableModule
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
    tick();
    expect(categoryService.getSubcategories).toHaveBeenCalledWith(1, 0);
    expect(component.subcategories).toEqual(getSubcategories());
  }));

  it('should open subcategories dialog', () => {
    component.openNew();

    expect(component.isSubcategoryDialogOpen).toBeTrue();
  });
  //
  it('should set subcategory for editing', () => {
    component.editSubcategory(getSubcategories()[0]);

    expect(component.editingSubcategory).toEqual(getSubcategories()[0]);
    expect(component.isSubcategoryDialogOpen).toBeTrue();
    expect(component.nameControl.value).toEqual(getSubcategories()[0].name);
  });

  it('should set subcategory for deletion', () => {
    const spyConfirm = spyOn(confirmationService, 'confirm').and.callFake(confirmation => {
      return confirmation.accept();
    });
    const spyDeletionConfirmed = spyOn(component, 'deletionConfirmed');

    component.deleteSubcategory(getSubcategories()[0]);

    expect(spyConfirm).toHaveBeenCalled();
    expect(spyDeletionConfirmed).toHaveBeenCalledWith(getSubcategories()[0]);
  });

  it('should delete subcategory after confirmation', fakeAsync(() => {
    const spyResetSubcategories = spyOn(component, 'resetSubcategories');

    component.deletionConfirmed(getSubcategories()[0]);

    tick();
    expect(categoryService.deleteSubcategory).toHaveBeenCalledWith(1);
    expect(spyAdd).toHaveBeenCalled();
    expect(spyResetSubcategories).toHaveBeenCalled();
  }));

  it('should fail deleting subcategory after confirmation', fakeAsync(() => {
    const spyResetSubcategories = spyOn(component, 'resetSubcategories');
    categoryService.deleteSubcategory = createSpy('deleteSubcategory').and
      .returnValue(throwError(null));
    component.deletionConfirmed(getSubcategories()[0]);

    tick();
    expect(categoryService.deleteSubcategory).toHaveBeenCalledWith(1);
    expect(spyAdd).toHaveBeenCalled();
    expect(spyResetSubcategories).not.toHaveBeenCalled();
  }));

  it('should reset table', fakeAsync(() => {
    const spyReset = spyOn(component.table, 'reset');
    component.resetSubcategories();

    tick();
    fixture.detectChanges();

    expect(component.subcategories.length).toEqual(0);
    expect(spyReset).toHaveBeenCalled();
  }));

  it('should save a new subcategory', fakeAsync(() => {
    const spyReset = spyOn(component, 'resetSubcategories');
    component.nameControl.setValue('Potkategorija');
    component.saveSubcategory();

    tick();
    fixture.detectChanges();

    expect(categoryService.createSubcategory)
      .toHaveBeenCalledWith(jasmine.objectContaining({name: 'Potkategorija'}));
    expect(spyAdd).toHaveBeenCalled();
    expect(spyReset).toHaveBeenCalled();
    expect(component.isSubcategoryDialogOpen).toBeFalse();
    expect(component.nameControl.dirty).toBeFalse();
    expect(component.category.numSubcategories).toEqual(3);
    expect(component.editingSubcategory).toBeUndefined();
  }));

  it('should save a editing subcategory', fakeAsync(() => {
    const spyReset = spyOn(component, 'resetSubcategories');
    component.editingSubcategory = getSubcategories()[0];
    component.nameControl.setValue('Potkategorija');
    component.saveSubcategory();

    tick();
    fixture.detectChanges();

    expect(categoryService.updateSubcategory)
      .toHaveBeenCalledWith(jasmine.objectContaining({name: 'Potkategorija'}));
    expect(spyAdd).toHaveBeenCalled();
    expect(spyReset).toHaveBeenCalled();
    expect(component.isSubcategoryDialogOpen).toBeFalse();
    expect(component.nameControl.dirty).toBeFalse();
    expect(component.category.numSubcategories).toEqual(2);
    expect(component.editingSubcategory).toBeUndefined();
  }));

  it('should get whether there is an editing category', () => {
    expect(component.editing).toBeFalse();

    component.editingSubcategory = getSubcategories()[0];

    expect(component.editing).toBeTrue();
  });
});

function getSubcategories(): Subcategory[] {
  return [
    {id: 1, categoryId: 1, name: 'potk1'},
    {id: 2, categoryId: 1, name: 'potk2'}
  ];
}

