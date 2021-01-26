import {ComponentFixture, fakeAsync, TestBed, tick} from '@angular/core/testing';

import {ReviewElementComponent} from './review-element.component';
import {Confirmation, ConfirmationService} from 'primeng/api';
import {of} from 'rxjs';
import {ReviewService} from '../../core/services/review/review.service';
import {AuthService} from '../../core/services/auth/auth.service';
import {ReviewGalleriaService} from '../../core/services/review-galleria/review-galleria.service';
import {GalleriaModule} from 'primeng/galleria';
import {Review} from '../../core/models/review';
import createSpy = jasmine.createSpy;
import * as moment from 'moment';

describe('ReviewElementComponent', () => {
  let component: ReviewElementComponent;
  let fixture: ComponentFixture<ReviewElementComponent>;
  let reviewService: ReviewService;
  let authService: AuthService;
  let galleriaService: ReviewGalleriaService;
  let confirmationService: ConfirmationService;

  beforeEach(async () => {
    const mockReviewService = {
      delete: createSpy('delete').and.returnValue(of(null))
    };
    const mockAuthService = {
      getUserRole: createSpy('getUserRole').and.returnValue('USER')
    };
    const mockGalleriaService = {
      value: [],
      activeIndex: 0,
      visible: false
    };
    await TestBed.configureTestingModule({
      imports: [GalleriaModule],
      declarations: [ ReviewElementComponent ],
      providers: [
        ConfirmationService,
        {provide: ReviewService, useValue: mockReviewService},
        {provide: AuthService, useValue: mockAuthService},
        {provide: ReviewGalleriaService, useValue: mockGalleriaService}
      ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    reviewService = TestBed.inject(ReviewService);
    authService = TestBed.inject(AuthService);
    galleriaService = TestBed.inject(ReviewGalleriaService);
    confirmationService = TestBed.inject(ConfirmationService);

    fixture = TestBed.createComponent(ReviewElementComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    component.review = {
      id: 1,
      rating: 3,
      comment: 'komentar',
      userId: 1,
      culturalOfferingId: 1,
      userFirstName: 'Ime',
      userLastName: 'Prezime',
      userEmail: 'imejl@mail.com'
    };
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should get user role from auth service', () => {
    const role = component.getUserRole();
    expect(role).toEqual('USER');
  });

  it('should open dialog', () => {
    const spyConfirm = spyOn(confirmationService, 'confirm').and.callFake((confirmation: Confirmation) => {
      return confirmation.accept();
    });
    const spyDeletionConfirmed = spyOn(component, 'reviewDeletionConfirmed');

    component.deleteReview();

    expect(spyConfirm).toHaveBeenCalled();
    expect(spyDeletionConfirmed).toHaveBeenCalled();
  });

  it('should confirm deletion and emit event', fakeAsync(() => {
    const spyEmit = spyOn(component.reviewDeleted, 'emit');
    component.reviewDeletionConfirmed();

    tick();
    fixture.detectChanges();

    expect(reviewService.delete).toHaveBeenCalledWith(1);
    expect(spyEmit).toHaveBeenCalled();
  }));

  it('should get relative time string', () => {
    expect(component.getAddedAgoString()).toEqual('Some time ago');

    component.review.timeAdded = moment();

    expect(component.getAddedAgoString()).toEqual('few seconds ago');
  });

  it('should get thumnail url for photo id', () => {
    expect(component.getThumbnailUrl(1)).toEqual('/photos/review/thumbnail/1.png');
  });

  it('should open galleria on click', () => {
    component.onClickReviewImg(1);

    expect(galleriaService.activeIndex).toEqual(1);
    expect(galleriaService.visible).toBeTrue();

  });

  it('should reset galleria on destroy', () => {
    component.ngOnDestroy();

    expect(galleriaService.value.length).toEqual(0);
    expect(galleriaService.visible).toBeFalse();
    expect(galleriaService.activeIndex).toEqual(0);
  });
});
