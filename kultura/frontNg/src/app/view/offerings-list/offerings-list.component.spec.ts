import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OfferingsListComponent } from './offerings-list.component';

describe('OfferingsListComponent', () => {
  let component: OfferingsListComponent;
  let fixture: ComponentFixture<OfferingsListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ OfferingsListComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(OfferingsListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
