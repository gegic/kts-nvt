import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OfferingRatingComponent } from './offering-rating.component';

describe('OfferingRatingComponent', () => {
  let component: OfferingRatingComponent;
  let fixture: ComponentFixture<OfferingRatingComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ OfferingRatingComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(OfferingRatingComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
