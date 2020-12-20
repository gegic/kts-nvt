import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ModeratorAddComponent } from './moderator-add.component';

describe('ModeratorAddComponent', () => {
  let component: ModeratorAddComponent;
  let fixture: ComponentFixture<ModeratorAddComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ModeratorAddComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ModeratorAddComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
