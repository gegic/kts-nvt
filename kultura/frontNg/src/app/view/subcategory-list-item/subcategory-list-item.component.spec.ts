import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SubcategoryListItemComponent } from './subcategory-list-item.component';

describe('SubcategoryListItemComponent', () => {
  let component: SubcategoryListItemComponent;
  let fixture: ComponentFixture<SubcategoryListItemComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SubcategoryListItemComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SubcategoryListItemComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
