import {ComponentFixture, fakeAsync, TestBed, tick} from '@angular/core/testing';

import {ModeratorsViewComponent} from './moderators-view.component';
import {of} from 'rxjs';
import {Moderator} from '../../core/models/moderator';
import {ModeratorService} from '../../core/services/moderator/moderator.service';
import {ConfirmationService, MessageService} from 'primeng/api';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {CardModule} from 'primeng/card';
import {DebugElement} from '@angular/core';
import {By} from '@angular/platform-browser';
import {ModeratorListElementComponent} from '../moderator-list-element/moderator-list-element.component';
import {RouterTestingModule} from '@angular/router/testing';
import {AvatarModule} from 'ngx-avatar';
import {HttpClientTestingModule} from '@angular/common/http/testing';

describe('ModeratorsViewComponent', () => {
  let component: ModeratorsViewComponent;
  let fixture: ComponentFixture<ModeratorsViewComponent>;
  let moderatorService: ModeratorService;

  beforeEach(async () => {
    const mockModerators = {
        content: [
          {
            id: '1',
            email: 'test1@mail.com',
            firstName: 'Firstname1',
            lastName: 'Lastname1',
          },
          {
            id: '2',
            email: 'test2@mail.com',
            firstName: 'Firstname2',
            lastName: 'Lastname2',
          },
          {
            id: '3',
            email: 'test3@mail.com',
            firstName: 'Firstname3',
            lastName: 'Lastname3',
          },
          {
            id: '4',
            email: 'test4@mail.com',
            firstName: 'Firstname4',
            lastName: 'Lastname4',
          }],
      pageable: {
          pageNumber: 1
      },
      totalPages: 5};

    const moderatorServiceMocked = {
      getModerators: jasmine.createSpy('getModerators').and.returnValue(of(mockModerators))
    };

    await TestBed.configureTestingModule({
      declarations: [ModeratorsViewComponent, ModeratorListElementComponent],
      providers:    [
        {provide: ModeratorService, useValue: moderatorServiceMocked },
      ConfirmationService,
      MessageService],
      imports: [AvatarModule, HttpClientTestingModule, ReactiveFormsModule, FormsModule, RouterTestingModule, CardModule]
    })
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ModeratorsViewComponent);
    component = fixture.componentInstance;
    moderatorService = TestBed.inject(ModeratorService);
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should fetch the moderators list on init', fakeAsync (() => {
    component.ngOnInit();
    expect(moderatorService.getModerators).toHaveBeenCalled();

    tick();

    fixture.whenStable()
      .then(() => {
        expect(component.moderators.length).toBe(4);
        fixture.detectChanges();
        const elements: DebugElement[] =
          fixture.debugElement.queryAll(By.css('.moderator'));
        expect(elements.length).toBe(4);
      });
  }));
});
