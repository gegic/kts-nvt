import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SubcategoriesViewComponent } from './subcategories-view.component';

describe('SubcategoriesViewComponent', () => {
  let component: SubcategoriesViewComponent;
  let fixture: ComponentFixture<SubcategoriesViewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SubcategoriesViewComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SubcategoriesViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
