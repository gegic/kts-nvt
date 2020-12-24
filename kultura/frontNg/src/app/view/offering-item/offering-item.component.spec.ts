import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OfferingItemComponent } from './offering-item.component';

describe('OfferingItemComponent', () => {
  let component: OfferingItemComponent;
  let fixture: ComponentFixture<OfferingItemComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ OfferingItemComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(OfferingItemComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
