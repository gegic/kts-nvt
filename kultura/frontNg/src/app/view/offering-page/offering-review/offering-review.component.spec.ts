import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OfferingReviewComponent } from './offering-review.component';

describe('OfferingRatingComponent', () => {
  let component: OfferingReviewComponent;
  let fixture: ComponentFixture<OfferingReviewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ OfferingReviewComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(OfferingReviewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
