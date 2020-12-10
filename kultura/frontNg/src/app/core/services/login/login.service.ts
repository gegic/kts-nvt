import {Injectable} from '@angular/core';
import {HttpClient, HttpErrorResponse, HttpEventType} from '@angular/common/http';
import {catchError, map} from 'rxjs/operators';
import {Router} from '@angular/router';
import {Observable} from 'rxjs';
import {User} from '../../models/user';

@Injectable({
  providedIn: 'root'
})
export class LoginService {

  email = '';
  name = '';
  password = '';

  constructor(private httpClient: HttpClient) {
  }

  login(): Observable<any> {
    return this.httpClient.post('/auth/login', {email: this.email, password: this.password})
      .pipe(map(user => {
        localStorage.setItem('user', JSON.stringify(user));
        return user;
      }));
  }

  checkExistence(email: string): Observable<any> {
    return this.httpClient.get(`/auth/exists/${email}`);
  }

  reset(): void {
    this.email = '';
    this.name = '';
    this.password = '';
  }
}
