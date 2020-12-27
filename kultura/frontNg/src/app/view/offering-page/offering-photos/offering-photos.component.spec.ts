import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OfferingPhotosComponent } from './offering-photos.component';

describe('OfferingPhotosComponent', () => {
  let component: OfferingPhotosComponent;
  let fixture: ComponentFixture<OfferingPhotosComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ OfferingPhotosComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(OfferingPhotosComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
