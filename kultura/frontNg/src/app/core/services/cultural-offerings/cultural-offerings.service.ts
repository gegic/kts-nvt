import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CulturalOfferingsService {

  constructor(private httpClient: HttpClient) { }

  delete(id: number): Observable<any> {
    return this.httpClient.delete(`/api/cultural-offerings/${id}`);
  }
}
