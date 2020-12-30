import { Injectable } from '@angular/core';
import {BehaviorSubject, Observable} from 'rxjs';
import {HttpClient} from '@angular/common/http';
import {CulturalOffering} from '../../models/cultural-offering';

@Injectable({
  providedIn: 'root'
})
export class CulturalOfferingDetailsService {

  culturalOffering: BehaviorSubject<CulturalOffering | undefined> = new BehaviorSubject<CulturalOffering | undefined>(undefined);

  constructor(private httpClient: HttpClient) { }

  getCulturalOffering(id: number): Observable<any> {
    return this.httpClient.get(`/api/cultural-offerings/${id}`);
  }
}
