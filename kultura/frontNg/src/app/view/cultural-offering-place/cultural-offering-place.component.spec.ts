import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CulturalOfferingPlaceComponent } from './cultural-offering-place.component';

describe('CulturalOfferingPlaceComponent', () => {
  let component: CulturalOfferingPlaceComponent;
  let fixture: ComponentFixture<CulturalOfferingPlaceComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CulturalOfferingPlaceComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CulturalOfferingPlaceComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
