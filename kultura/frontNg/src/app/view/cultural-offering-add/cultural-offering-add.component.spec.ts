import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CulturalOfferingAddComponent } from './cultural-offering-add.component';

describe('CulturalOfferingAddComponent', () => {
  let component: CulturalOfferingAddComponent;
  let fixture: ComponentFixture<CulturalOfferingAddComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CulturalOfferingAddComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CulturalOfferingAddComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
