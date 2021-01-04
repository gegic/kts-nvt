import { Injectable } from '@angular/core';
import {Observable} from 'rxjs';
import {HttpClient} from '@angular/common/http';
import {Review} from '../../models/review';
import {ReviewNumbers} from '../../models/reviewNumbers';

@Injectable({
  providedIn: 'root'
})
export class ReviewService {

  reviews: Review[] = [];
  reviewNumbers: ReviewNumbers = new ReviewNumbers();

  constructor(private httpClient: HttpClient) { }

  getReviews(culturalOfferingId: number, page: number): Observable<any> {
    return this.httpClient.get(`/api/reviews/cultural-offering/${culturalOfferingId}?page=${page}`);
  }

  getReviewNumbers(culturalOfferingId: number): Observable<any> {
    return this.httpClient.get(`/api/reviews/by-rating/cultural-offering/${culturalOfferingId}`);
  }

  getReviewForUser(culturalOfferingId: number, userId: number): Observable<any> {
    return this.httpClient.get(`/api/reviews/cultural-offering/${culturalOfferingId}/user/${userId}`);
  }

  delete(reviewId: number): Observable<any> {
    return this.httpClient.delete(`/api/reviews/${reviewId}`);
  }

  deletePhotosByCulturalOfferingAndUser(reviewId: number): Observable<any> {
    return this.httpClient.delete(`/api/reviews/photos/id/${reviewId}`);
  }

  add(review: Review): Observable<any> {
    return this.httpClient.post('/api/reviews', review);
  }

  edit(review: Review): Observable<any> {
    return this.httpClient.put('/api/reviews', review);
  }

  clearPhotos(): Observable<any> {
    return this.httpClient.delete('/api/reviews/clear-photos');
  }

  addPhotos(photos: File[]): Observable<any> {
    const formData = new FormData();
    for (const photo of photos) {
      formData.append('photos', photo);
    }
    return this.httpClient.post('/api/reviews/add-photos', formData);
  }
}
