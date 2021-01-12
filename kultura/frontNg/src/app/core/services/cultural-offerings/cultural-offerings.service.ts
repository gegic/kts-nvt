import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {BehaviorSubject, Observable} from 'rxjs';
import {CulturalOffering} from '../../models/cultural-offering';

@Injectable({
  providedIn: 'root'
})
export class CulturalOfferingsService {

  culturalOfferings: CulturalOffering[] = [];
  searchQuery: BehaviorSubject<string> = new BehaviorSubject<string>('');
  rating?: {min: number, max: number};

  constructor(private httpClient: HttpClient) { }

  getCulturalOfferings(page: number, sort: string): Observable<any> {
    let apiUrl = `/api/cultural-offerings?page=${page}&sort=${sort}`;
    if (!!this.searchQuery) {
      apiUrl += `&search=${this.searchQuery.getValue()}`;
    }
    if (!!this.rating) {
      apiUrl += `&rating-min=${this.rating.min}&rating-max=${this.rating.max}`;
    }
    return this.httpClient.get(apiUrl);

  }

  delete(id: number): Observable<any> {
    return this.httpClient.delete(`/api/cultural-offerings/${id}`);
  }
}
