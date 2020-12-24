import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OfferingSidebarComponent } from './offering-sidebar.component';

describe('OfferingSidebarComponent', () => {
  let component: OfferingSidebarComponent;
  let fixture: ComponentFixture<OfferingSidebarComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ OfferingSidebarComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(OfferingSidebarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
