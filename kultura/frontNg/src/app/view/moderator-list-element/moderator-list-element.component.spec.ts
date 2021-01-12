import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ModeratorListElementComponent } from './moderator-list-element.component';

describe('ModeratorListElementComponent', () => {
  let component: ModeratorListElementComponent;
  let fixture: ComponentFixture<ModeratorListElementComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ModeratorListElementComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ModeratorListElementComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
