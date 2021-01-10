import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CulturalOfferingDetailsComponent } from './cultural-offering-details.component';

describe('CulturalOfferingDetailsComponent', () => {
  let component: CulturalOfferingDetailsComponent;
  let fixture: ComponentFixture<CulturalOfferingDetailsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CulturalOfferingDetailsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CulturalOfferingDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
