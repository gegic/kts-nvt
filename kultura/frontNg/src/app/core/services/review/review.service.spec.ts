import {fakeAsync, getTestBed, TestBed, tick} from '@angular/core/testing';

import {ReviewService} from './review.service';
import {HttpClientTestingModule, HttpTestingController} from '@angular/common/http/testing';
import {HttpClient, HttpErrorResponse} from '@angular/common/http';
import {Review} from '../../models/review';

describe('ReviewService', () => {
  let service: ReviewService;
  let injector;
  let httpMock: HttpTestingController;
  let httpClient: HttpClient;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [ReviewService]
    });
    service = TestBed.inject(ReviewService);
    injector = getTestBed();
    httpClient = TestBed.inject(HttpClient);
    httpMock = TestBed.inject(HttpTestingController);
  });

  it('should be created', () => {
    expect(service).toBeDefined();
  });

  it('getReviews() should query url and get all reviews for specific cultural offerings', fakeAsync(() => {
    let reviews: Review[] = [];
    const mockReview: Review[] = [
      {
        id: 1,
        rating: 3,
        comment: 'Nije ovo lose',
        culturalOfferingId: 1,
        userId: 1,
        userFirstName: 'Firstname1',
        userLastName: 'Lastname1',
        userEmail: 'mail1@mail.com'
      },
      {
        id: 2,
        rating: 3,
        comment: 'Nije ovo lose',
        culturalOfferingId: 1,
        userId: 2,
        userFirstName: 'Firstname2',
        userLastName: 'Lastname2',
        userEmail: 'mail2@mail.com'
      },
      {
        id: 3,
        rating: 3,
        comment: 'Nije ovo lose',
        culturalOfferingId: 1,
        userId: 3,
        userFirstName: 'Firstname3',
        userLastName: 'Lastname3',
        userEmail: 'mail3@mail.com'
      }];

    service.getReviews(1, 1).subscribe(data => {
      reviews = data;
    });

    const req = httpMock.expectOne('/api/reviews/cultural-offering/1?page=1');
    expect(req.request.method).toBe('GET');
    req.flush(mockReview);

    tick();

    expect(reviews.length).toEqual(3, 'should contain given amount of reviews for specific cultural offerings');

    expect(reviews[0].id).toEqual(1);
    expect(reviews[0].comment).toEqual('Nije ovo lose');
    expect(reviews[0].culturalOfferingId).toEqual(1);
    expect(reviews[0].rating).toEqual(3);
    expect(reviews[0].userId).toEqual(1);
    expect(reviews[0].userFirstName).toEqual('Firstname1');
    expect(reviews[0].userLastName).toEqual('Lastname1');
    expect(reviews[0].userEmail).toEqual('mail1@mail.com');

    expect(reviews[1].id).toEqual(2);
    expect(reviews[1].comment).toEqual('Nije ovo lose');
    expect(reviews[1].culturalOfferingId).toEqual(1);
    expect(reviews[1].rating).toEqual(3);
    expect(reviews[1].userId).toEqual(2);
    expect(reviews[1].userFirstName).toEqual('Firstname2');
    expect(reviews[1].userLastName).toEqual('Lastname2');
    expect(reviews[1].userEmail).toEqual('mail2@mail.com');

    expect(reviews[2].id).toEqual(3);
    expect(reviews[2].comment).toEqual('Nije ovo lose');
    expect(reviews[2].culturalOfferingId).toEqual(1);
    expect(reviews[2].rating).toEqual(3);
    expect(reviews[2].userId).toEqual(3);
    expect(reviews[2].userFirstName).toEqual('Firstname3');
    expect(reviews[2].userLastName).toEqual('Lastname3');
    expect(reviews[2].userEmail).toEqual('mail3@mail.com');
  }));

  it('getReviewNumbers() should query url and get all reviews by rating', fakeAsync(() => {
    let reviews: Review[] = [];
    const mockReview: Review[] = [
      {
        id: 1,
        rating: 3,
        comment: 'Nije ovo lose',
        culturalOfferingId: 1,
        userId: 1,
        userFirstName: 'Firstname1',
        userLastName: 'Lastname1',
        userEmail: 'mail1@mail.com'
      },
      {
        id: 2,
        rating: 3,
        comment: 'Nije ovo lose',
        culturalOfferingId: 1,
        userId: 2,
        userFirstName: 'Firstname2',
        userLastName: 'Lastname2',
        userEmail: 'mail2@mail.com'
      },
      {
        id: 3,
        rating: 3,
        comment: 'Nije ovo lose',
        culturalOfferingId: 1,
        userId: 3,
        userFirstName: 'Firstname3',
        userLastName: 'Lastname3',
        userEmail: 'mail3@mail.com'
      }];

    service.getReviewNumbers(1).subscribe(data => {
      reviews = data;
    });

    const req = httpMock.expectOne('/api/reviews/by-rating/cultural-offering/1');
    expect(req.request.method).toBe('GET');
    req.flush(mockReview);

    tick();

    expect(reviews.length).toEqual(3, 'should contain given amount of reviews for specific cultural offerings');

    expect(reviews[0].id).toEqual(1);
    expect(reviews[0].comment).toEqual('Nije ovo lose');
    expect(reviews[0].culturalOfferingId).toEqual(1);
    expect(reviews[0].rating).toEqual(3);
    expect(reviews[0].userId).toEqual(1);
    expect(reviews[0].userFirstName).toEqual('Firstname1');
    expect(reviews[0].userLastName).toEqual('Lastname1');
    expect(reviews[0].userEmail).toEqual('mail1@mail.com');

    expect(reviews[1].id).toEqual(2);
    expect(reviews[1].comment).toEqual('Nije ovo lose');
    expect(reviews[1].culturalOfferingId).toEqual(1);
    expect(reviews[1].rating).toEqual(3);
    expect(reviews[1].userId).toEqual(2);
    expect(reviews[1].userFirstName).toEqual('Firstname2');
    expect(reviews[1].userLastName).toEqual('Lastname2');
    expect(reviews[1].userEmail).toEqual('mail2@mail.com');

    expect(reviews[2].id).toEqual(3);
    expect(reviews[2].comment).toEqual('Nije ovo lose');
    expect(reviews[2].culturalOfferingId).toEqual(1);
    expect(reviews[2].rating).toEqual(3);
    expect(reviews[2].userId).toEqual(3);
    expect(reviews[2].userFirstName).toEqual('Firstname3');
    expect(reviews[2].userLastName).toEqual('Lastname3');
    expect(reviews[2].userEmail).toEqual('mail3@mail.com');
  }));

  it('getReviewForUser() should query url and get all reviews for specific users', fakeAsync(() => {
    let reviews: Review[] = [];
    const mockReview: Review[] = [
      {
        id: 1,
        rating: 3,
        comment: 'Nije ovo lose',
        culturalOfferingId: 1,
        userId: 1,
        userFirstName: 'Firstname1',
        userLastName: 'Lastname1',
        userEmail: 'mail1@mail.com'
      },
      {
        id: 2,
        rating: 3,
        comment: 'Nije ovo lose2',
        culturalOfferingId: 1,
        userId: 1,
        userFirstName: 'Firstname1',
        userLastName: 'Lastname1',
        userEmail: 'mail1@mail.com'
      },
      {
        id: 3,
        rating: 3,
        comment: 'Nije ovo lose3',
        culturalOfferingId: 1,
        userId: 1,
        userFirstName: 'Firstname1',
        userLastName: 'Lastname1',
        userEmail: 'mail1@mail.com'
      }];

    service.getReviewForUser(1, 1).subscribe(data => {
      reviews = data;
    });

    const req = httpMock.expectOne('/api/reviews/cultural-offering/1/user/1');
    expect(req.request.method).toBe('GET');
    req.flush(mockReview);

    tick();

    expect(reviews.length).toEqual(3, 'should contain given amount of reviews for specific cultural offerings');

    expect(reviews[0].id).toEqual(1);
    expect(reviews[0].comment).toEqual('Nije ovo lose');
    expect(reviews[0].culturalOfferingId).toEqual(1);
    expect(reviews[0].rating).toEqual(3);
    expect(reviews[0].userId).toEqual(1);
    expect(reviews[0].userFirstName).toEqual('Firstname1');
    expect(reviews[0].userLastName).toEqual('Lastname1');
    expect(reviews[0].userEmail).toEqual('mail1@mail.com');

    expect(reviews[1].id).toEqual(2);
    expect(reviews[1].comment).toEqual('Nije ovo lose2');
    expect(reviews[1].culturalOfferingId).toEqual(1);
    expect(reviews[1].rating).toEqual(3);
    expect(reviews[0].userId).toEqual(1);
    expect(reviews[0].userFirstName).toEqual('Firstname1');
    expect(reviews[0].userLastName).toEqual('Lastname1');
    expect(reviews[0].userEmail).toEqual('mail1@mail.com');

    expect(reviews[2].id).toEqual(3);
    expect(reviews[2].comment).toEqual('Nije ovo lose3');
    expect(reviews[2].culturalOfferingId).toEqual(1);
    expect(reviews[2].rating).toEqual(3);
    expect(reviews[0].userId).toEqual(1);
    expect(reviews[0].userFirstName).toEqual('Firstname1');
    expect(reviews[0].userLastName).toEqual('Lastname1');
    expect(reviews[0].userEmail).toEqual('mail1@mail.com');
  }));

  it('addReview()  should query url and save a review', fakeAsync(() => {
    let newReview: Review = {
      rating: 3,
      comment: 'Nije ovo lose',
      culturalOfferingId: 1,
      userId: 1,
      userFirstName: 'Firstname1',
      userLastName: 'Lastname1',
      userEmail: 'mail1@mail.com'
    };

    const mockReview: Review =
      {
        id: 1,
        rating: 3,
        comment: 'Nije ovo lose',
        culturalOfferingId: 1,
        userId: 1,
        userFirstName: 'Firstname1',
        userLastName: 'Lastname1',
        userEmail: 'mail1@mail.com'
      };

    service.add(newReview).subscribe(data => newReview = data);


    const req = httpMock.expectOne('/api/reviews');
    expect(req.request.method).toBe('POST');
    req.flush(mockReview);

    tick();

    expect(newReview.id).toEqual(1);
    expect(newReview.comment).toEqual('Nije ovo lose');
    expect(newReview.culturalOfferingId).toEqual(1);
    expect(newReview.rating).toEqual(3);
    expect(newReview.userId).toEqual(1);
    expect(newReview.userFirstName).toEqual('Firstname1');
    expect(newReview.userLastName).toEqual('Lastname1');
    expect(newReview.userEmail).toEqual('mail1@mail.com');
  }));

  it('edit()  should query url and edit a review', fakeAsync(() => {
    let newReview: Review = {
      rating: 3,
      comment: 'Nije ovo lose',
      culturalOfferingId: 1,
      userId: 1,
      userFirstName: 'Firstname1',
      userLastName: 'Lastname1',
      userEmail: 'mail1@mail.com'
    };

    const mockReview: Review =
      {
        id: 1,
        rating: 3,
        comment: 'Nije ovo lose',
        culturalOfferingId: 1,
        userId: 1,
        userFirstName: 'Firstname1',
        userLastName: 'Lastname1',
        userEmail: 'mail1@mail.com'
      };

    service.edit(newReview).subscribe(data => newReview = data);


    const req = httpMock.expectOne('/api/reviews');
    expect(req.request.method).toBe('PUT');
    req.flush(mockReview);

    tick();

    expect(newReview.id).toEqual(1);
    expect(newReview.comment).toEqual('Nije ovo lose');
    expect(newReview.culturalOfferingId).toEqual(1);
    expect(newReview.rating).toEqual(3);
    expect(newReview.userId).toEqual(1);
    expect(newReview.userFirstName).toEqual('Firstname1');
    expect(newReview.userLastName).toEqual('Lastname1');
    expect(newReview.userEmail).toEqual('mail1@mail.com');
  }));

  it('delete() should query url and delete a review', () => {
    service.delete(1).subscribe();

    const req = httpMock.expectOne(`/api/reviews/1`);
    expect(req.request.method).toBe('DELETE');
    req.flush({});
  });


  it('deletePhotosByCulturalOfferingAndUser() should query url and delete photos by cultural offering and users', () => {
    service.deletePhotosByCulturalOfferingAndUser(1).subscribe();

    const req = httpMock.expectOne(`/api/reviews/photos/id/1`);
    expect(req.request.method).toBe('DELETE');
    req.flush({});
  });

  it('addPhoto()  should query url and add a photo', fakeAsync(() => {
    let newPhotos: File[] = [
      new File([''], 'photo1'),
      new File([''], 'photo2'),
      new File([''], 'photo3'),
      new File([''], 'photo4'),
    ];

    const mockReviewsPhotos: File[] = [
      new File([''], 'photo1'),
      new File([''], 'photo2'),
      new File([''], 'photo3'),
      new File([''], 'photo4'),
    ];


    service.addPhotos(newPhotos).subscribe(data => {
      newPhotos = data;
      const req = httpMock.expectOne('/api/reviews/add-photos');
      expect(req.request.method).toBe('POST');
      req.flush(mockReviewsPhotos);

      tick();

      expect(newPhotos).toBeDefined();
      expect(newPhotos.length).toEqual(4);
      expect(newPhotos[0].name).toEqual('photo1');
      expect(newPhotos[1].name).toEqual('photo2');
      expect(newPhotos[2].name).toEqual('photo3');
      expect(newPhotos[3].name).toEqual('photo4');
    });
  }));

  it('clearPhotos() should query url and clear a photos', () => {
    service.clearPhotos().subscribe();

    const req = httpMock.expectOne(`/api/reviews/clear-photos`);
    expect(req.request.method).toBe('DELETE');
    req.flush({});
  });

  it('should throw error', () => {
    let error: HttpErrorResponse;
    const mockReview: Review =
      {
        id: 1,
        rating: 3,
        comment: 'Nije ovo lose',
        culturalOfferingId: 1,
        userId: 1,
        userFirstName: 'Firstname1',
        userLastName: 'Lastname1',
        userEmail: 'mail1@mail.com'
      };

    service.add(mockReview).subscribe(null, e => {
      error = e;
    });
    const req = httpMock.expectOne('/api/reviews');
    expect(req.request.method).toBe('POST');

    req.flush('Something went wrong', {
      status: 404,
      statusText: 'Network error'
    });

    expect(error.statusText).toEqual('Network error');
    expect(error.status).toEqual(404);
  });

});
