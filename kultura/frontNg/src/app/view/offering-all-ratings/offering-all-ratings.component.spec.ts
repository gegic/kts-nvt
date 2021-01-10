import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OfferingAllRatingsComponent } from './offering-all-ratings.component';

describe('OfferingAllRatingsComponent', () => {
  let component: OfferingAllRatingsComponent;
  let fixture: ComponentFixture<OfferingAllRatingsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ OfferingAllRatingsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(OfferingAllRatingsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
