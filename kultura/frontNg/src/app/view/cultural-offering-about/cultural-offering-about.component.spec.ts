import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CulturalOfferingAboutComponent } from './cultural-offering-about.component';

describe('CulturalOfferingAboutComponent', () => {
  let component: CulturalOfferingAboutComponent;
  let fixture: ComponentFixture<CulturalOfferingAboutComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CulturalOfferingAboutComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CulturalOfferingAboutComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
