import {ComponentFixture, fakeAsync, TestBed, tick} from '@angular/core/testing';

import {ReviewsComponent} from './reviews.component';
import {AuthService} from '../../core/services/auth/auth.service';
import {ConfirmationService, MessageService} from 'primeng/api';
import {CulturalOfferingDetailsService} from '../../core/services/cultural-offering-details/cultural-offering-details.service';
import {ReviewService} from '../../core/services/review/review.service';
import {Router} from '@angular/router';
import {DialogService} from 'primeng/dynamicdialog';
import {ReviewGalleriaService} from '../../core/services/review-galleria/review-galleria.service';

import {RatingModule} from 'primeng/rating';
import {ProgressBarModule} from 'primeng/progressbar';
import {InfiniteScrollModule} from 'ngx-infinite-scroll';
import {ProgressSpinnerModule} from 'primeng/progressspinner';
import {FileUploadModule} from 'primeng/fileupload';
import {GalleriaModule} from 'primeng/galleria';
import {DialogModule} from 'primeng/dialog';
import {BehaviorSubject, of} from 'rxjs';
import {CulturalOffering} from '../../core/models/cultural-offering';
import {Review} from '../../core/models/review';
import {ReviewNumbers} from '../../core/models/reviewNumbers';
import {User} from '../../core/models/user';
import {Authority} from '../../core/models/authority';
import {By} from '@angular/platform-browser';
import * as moment from 'moment-timezone';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {ReviewElementComponent} from '../review-element/review-element.component';
import {MenuModule} from 'primeng/menu';
import createSpy = jasmine.createSpy;
import {HttpClient} from '@angular/common/http';
import {HttpClientTestingModule} from '@angular/common/http/testing';


describe('ReviewsComponent', () => {
  let component: ReviewsComponent;
  let fixture: ComponentFixture<ReviewsComponent>;


  let detailsService: CulturalOfferingDetailsService;
  let reviewService: ReviewService;
  let messageService: MessageService;
  let router: Router;
  let confirmationService: ConfirmationService;
  let dialogService: DialogService;
  let authService: AuthService;
  let reviewGalleriaService: ReviewGalleriaService;

  let message = '';


  beforeEach(async () => {
    const detailsServiceMock = {
      culturalOffering: new BehaviorSubject<CulturalOffering>(getCulturalOffering()),
      getCulturalOffering: createSpy('getCulturalOffering').and.returnValue(of(getCulturalOffering()))
    };

    const reviewServiceMock = {
      reviews: [],
      reviewNumbers: new ReviewNumbers(),
      getReviewForUser: createSpy('getReviewForUser').and.returnValue(of(getReview())),
      getReviews: createSpy('getReviews').and.returnValue(of(getReviews())),
      addPhotos: createSpy('addPhotos').and.returnValue(of([])),
      clearPhotos: createSpy('clearPhotos').and.returnValue(of()),
      getReviewNumbers: createSpy('clearPhotos').and.returnValue(of(getReviewNums())),
      add: createSpy('add').and.callFake((val) => of(val)),
      edit: createSpy('edit').and.callFake((val) => of(val)),

    };


    const messageServiceMock = {
      add: createSpy('add').and.callFake((msg: any): void => {
        message = msg.severity;
      })
    };

    const routerMock = {
      navigate: createSpy('navigate')
    };


    const authServiceMock = {
      user: new BehaviorSubject<User | null>(getLoggedUser()),
      updateUserData: () => {
      },
      isLoggedIn: () => true,
      getUserRole: createSpy('getUserRole').and.returnValue('USER')
    };

    const dialogServiceMocked = {
      open: jasmine.createSpy('open').and.returnValue(of({}))
    };

    await TestBed.configureTestingModule({
      declarations: [ReviewsComponent, ReviewElementComponent],
      imports: [
        RatingModule,
        ProgressBarModule,
        InfiniteScrollModule,
        ProgressSpinnerModule,
        FileUploadModule,
        GalleriaModule,
        FormsModule,
        ReactiveFormsModule,
        DialogModule,
        BrowserAnimationsModule,
        MenuModule
      ],
      providers: [
        {provide: CulturalOfferingDetailsService, useValue: detailsServiceMock},
        {provide: ReviewService, useValue: reviewServiceMock},
        {provide: MessageService, useValue: messageServiceMock},
        {provide: ConfirmationService, useValue: ConfirmationService},
        {provide: DialogService, useValue: dialogServiceMocked},
        {provide: Router, useValue: routerMock},
        {provide: AuthService, useValue: authServiceMock},
        {provide: HttpClient, useValue: HttpClientTestingModule},
        {provide: ReviewGalleriaService, useValue: ReviewGalleriaService},
      ]
    })
      .compileComponents();
  });

  beforeEach(() => {
    messageService = TestBed.inject(MessageService);
    detailsService = TestBed.inject(CulturalOfferingDetailsService);
    reviewGalleriaService = TestBed.inject(ReviewGalleriaService);
    router = TestBed.inject(Router);
    reviewService = TestBed.inject(ReviewService);
    confirmationService = TestBed.inject(ConfirmationService);
    dialogService = TestBed.inject(DialogService);
    authService = TestBed.inject(AuthService);

    fixture = TestBed.createComponent(ReviewsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    component.ngOnInit();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
    expect(component).toBeDefined();
  });

  it('should display review info', fakeAsync(() => {
    const reviewNum = fixture.debugElement.query(By.css('.num-reviews'));
    expect(reviewNum.nativeElement.textContent).toEqual('3');

    const overallRating = fixture.debugElement.query(By.css('.rating'));
    expect(overallRating.nativeElement.textContent).toEqual('3.1');
  }));

  it('should edit the review', fakeAsync(() => {
    fixture.debugElement.query(By.css('#review-button')).nativeElement.click();
    tick();
    fixture.detectChanges();

    expect(component.isAddDialogOpen).toBeTrue();
    component.userReview.comment = 'Review done.';

    fixture.debugElement.queryAll(By.css('#rating-stars-edit .pi-star-o'))[4].nativeElement.click();

    fixture.debugElement.query(By.css('#review-submit')).nativeElement.click();
    tick();
    fixture.detectChanges();

    fixture.debugElement.query(By.css('#review-button')).nativeElement.click();
    tick();
    fixture.detectChanges();
    expect(component.userReview.comment).toEqual('Review done.');
    expect(fixture.debugElement.query(By.css('#review-comment')).nativeElement.value).toEqual('Review done.');
    expect(component.userReview.rating).toEqual(5);
    expect(fixture.debugElement.queryAll(By.css('#rating-stars-edit .pi-star-o')).length).toEqual(0);
  }));

  it('should fail to review without rating', fakeAsync(() => {
    fixture.debugElement.query(By.css('#review-button')).nativeElement.click();
    tick();
    fixture.detectChanges();
    fixture.debugElement.query(By.css('#rating-stars-edit .pi-ban')).nativeElement.click();

    fixture.debugElement.query(By.css('#review-submit')).nativeElement.click();
    tick();
    fixture.detectChanges();

    expect(message).toEqual('error');
  }));

  it('should succeed to upload 3 photos', fakeAsync(() => {
    const event = {
      files: [
        mockOneFile(),
        mockOneFile(),
        mockOneFile(),
      ]
    };

    component.onUploadStart(event);
    tick();
    fixture.detectChanges();

    expect(message).toEqual('success');
  }));

  it('should fail to upload more than 3 photos', fakeAsync(() => {
    const event = {
      files: [
        mockOneFile(),
        mockOneFile(),
        mockOneFile(),
        mockOneFile()
      ]
    };

    component.onUploadStart(event);
    tick();
    fixture.detectChanges();

    expect(message).toEqual('error');
  }));
});


function getLoggedUser(): User {
  const u = new User();
  u.id = 3;
  u.email = emails[2];
  u.lastName = lastNames[2];
  u.firstName = names[2];
  const a: Authority = new Authority();
  a.authority = 'USER';
  u.authorities = [a];
  return u;
}

function getCulturalOffering(): CulturalOffering {
  const cu = new CulturalOffering();
  cu.additionalInfo = 'Lorem ipsum';
  cu.address = 'Gandijeva 8';
  cu.briefInfo = 'Veni vidi vici. Alea iackta est';
  cu.categoryId = 1;
  cu.categoryName = 'Dogadjaj';
  cu.id = 1;
  cu.latitude = 120;
  cu.longitude = 120;
  cu.name = 'Vasar';
  cu.numSubscribed = 0;
  cu.numReviews = 3;
  cu.overallRating = 3.1;
  cu.photoId = 1;
  cu.subcategoryId = 1;
  cu.subcategoryName = 'Mali Vasar';
  return cu;
}

const comments: string[] = ['Best vasar ever', 'Worst vasar in balkans', 'Bless this vasar'];
const names: string[] = ['John', 'Oliver', 'Tommy'];
const lastNames: string[] = ['Smith', 'Twist', 'Gun'];
const emails: string[] = ['john@mail.com', 'oliver@mail.com', 'tommy@mail.com'];
const ratings: number[] = [1, 2, 3];

function getReviews(): any {
  const reviews: Review[] = [];
  let rid = 0;
  for (const c of comments) {
    const r: Review = new Review();
    r.comment = c;
    r.id = rid;
    r.culturalOfferingId = 3;
    r.photos = [1, 2];
    r.timeAdded = moment(new Date());
    r.userId = rid + 1;
    r.userFirstName = names[rid];
    r.userLastName = lastNames[rid];
    r.userEmail = emails[rid];
    r.rating = ratings[rid];
    reviews.push(r);
    rid++;
  }
  return {
    content: reviews,
    pageable: {
      pageNumber: 0
    },
    totalPages: 1
  };
}

function getReview(): Review {

  const r: Review = new Review();
  r.comment = comments[2];
  r.id = 99;
  r.culturalOfferingId = 1;
  r.photos = [1, 2];
  r.timeAdded = moment();
  r.userId = 3;
  r.userFirstName = names[2];
  r.userLastName = lastNames[2];
  r.userEmail = emails[2];
  r.rating = ratings[2];
  return r;
}

function getReviewNums(): any[] {
  return [
    {rating: 1, numReviews: 100},
    {rating: 2, numReviews: 110},
    {rating: 3, numReviews: 200},
    {rating: 4, numReviews: 103},
    {rating: 5, numReviews: 140},
  ];
}


function mockOneFile(): File {
  const blob = new Blob([''], {type: 'image/png'});
  // blob.lastModifiedDate = '';
  // blob.name = 'filename';
  return blob as File;
}
