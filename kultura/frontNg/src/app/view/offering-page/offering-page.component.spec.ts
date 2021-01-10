import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OfferingPageComponent } from './offering-page.component';

describe('OfferingPageComponent', () => {
  let component: OfferingPageComponent;
  let fixture: ComponentFixture<OfferingPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ OfferingPageComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(OfferingPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
