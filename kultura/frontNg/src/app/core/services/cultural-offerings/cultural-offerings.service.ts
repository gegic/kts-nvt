import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {CulturalOffering} from '../../models/cultural-offering';

@Injectable({
  providedIn: 'root'
})
export class CulturalOfferingsService {

  culturalOfferings: CulturalOffering[] = [];

  constructor(private httpClient: HttpClient) { }

  getCulturalOfferings(page: number, sort: string): Observable<any> {
    return this.httpClient.get(`/api/cultural-offerings?page=${page}&sort=${sort}`);
  }

  delete(id: number): Observable<any> {
    return this.httpClient.delete(`/api/cultural-offerings/${id}`);
  }
}
