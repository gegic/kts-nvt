import { Injectable } from '@angular/core';
import {Observable} from 'rxjs';
import {HttpClient} from '@angular/common/http';
import {Review} from '../../models/review';
import {ReviewNumbers} from '../../models/reviewNumbers';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';


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
  httpClient:HttpClient;
  constructor(httpClient:HttpClient) {
    this.httpClient = httpClient;
  }
  
  getSummary(id: Number) {

    // return this.httpClient.get("/api/reviews/cultural-offering/summary/"+id);

    return {
      ratings:{
        1:140,
        2:120,
        3:200,
        4:230,
        5:150
      }
    };
  }

  getReviews(offering_id:Number){
    return [
      {
        userFirstName:"Miloje Puzigaca",
        timeAdded:new Date(),
        rating:3,
        comment:"Lorem ipsum dolor sit amet, consectetur adipiscing elit. Ut aliquam magna non ipsum imperdiet, id commodo ante hendrerit. Pellentesque condimentum lorem sed finibus malesuada. Fusce dictum posuere purus ut dictum. Praesent sit amet facilisis lacus."
      },
      {
        userFirstName:"Miloje Puzigaca",
        timeAdded:new Date(),
        rating:3,
        comment:"Lorem ipsum dolor sit amet, consectetur adipiscing elit. Ut aliquam magna non ipsum imperdiet, id commodo ante hendrerit. Pellentesque condimentum lorem sed finibus malesuada. Fusce dictum posuere purus ut dictum. Praesent sit amet facilisis lacus."
      },
      {
        userFirstName:"Miloje Puzigaca",
        timeAdded:new Date(),
        rating:3,
        comment:"Lorem ipsum dolor sit amet, consectetur adipiscing elit. Ut aliquam magna non ipsum imperdiet, id commodo ante hendrerit. Pellentesque condimentum lorem sed finibus malesuada. Fusce dictum posuere purus ut dictum. Praesent sit amet facilisis lacus."
      },
    ]

  }
}
