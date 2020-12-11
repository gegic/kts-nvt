import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class VerifyService {

  name = '';

  constructor(private httpClient: HttpClient) {
  }

  checkExistence(id: string): Observable<any> {
    return this.httpClient.get(`/auth/exists/verify/id/${id}`);
  }

  verify(id: string): Observable<any> {
    return this.httpClient.get(`/auth/verify/${id}`);
  }
}
