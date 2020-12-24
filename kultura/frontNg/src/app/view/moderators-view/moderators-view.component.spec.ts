import {ComponentFixture, TestBed} from '@angular/core/testing';

import {ModeratorsViewComponent} from './moderators-view.component';

describe('ModeratorsViewComponent', () => {
  let component: ModeratorsViewComponent;
  let fixture: ComponentFixture<ModeratorsViewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ModeratorsViewComponent]
    })
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ModeratorsViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
