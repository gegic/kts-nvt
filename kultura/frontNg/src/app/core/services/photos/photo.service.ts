import {Injectable} from '@angular/core';
import {CulturalOfferingPhoto} from '../../models/culturalOfferingPhoto';
import {Observable} from 'rxjs';
import {HttpClient} from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class PhotoService {

  photos: CulturalOfferingPhoto[] = [];

  constructor(private httpClient: HttpClient) {
  }

  getPhotos(culturalOfferingId: number, page: number): Observable<any> {
    return this.httpClient.get(`/api/photos/cultural-offering/${culturalOfferingId}?page=${page}`);
  }

  addPhoto(file: File, culturalOfferingId: number): Observable<any> {
    const formData = new FormData();
    formData.append('photo', file);
    return this.httpClient.post(`/api/photos/cultural-offering/${culturalOfferingId}`, formData);
  }

  delete(photoId: number): Observable<any> {
    return this.httpClient.delete(`/api/photos/${photoId}`);
  }
}
