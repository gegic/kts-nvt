import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OfferingRatingsListComponent } from './offering-ratings-list.component';

describe('OfferingRatingsListComponent', () => {
  let component: OfferingRatingsListComponent;
  let fixture: ComponentFixture<OfferingRatingsListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ OfferingRatingsListComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(OfferingRatingsListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
