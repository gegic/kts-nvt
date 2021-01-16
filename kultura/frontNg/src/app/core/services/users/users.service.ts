import { Injectable } from '@angular/core';
import { User } from '../../models/user';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private httpClient: HttpClient) { }


  update(user: User): Observable<any> {
    return this.httpClient.put('/api/users', user);
  }

  getById(id: number): Observable<any>{
    return this.httpClient.get(`/api/users/${id}`);
  }
}
