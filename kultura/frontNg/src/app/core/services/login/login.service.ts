import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {map} from 'rxjs/operators';
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
        if ((user as User).verified) {
          localStorage.setItem('user', JSON.stringify(user));
        }
        return user;
      }));
  }

  checkExistence(email: string): Observable<any> {
    return this.httpClient.get(`/auth/exists/email/${email}`);
  }

  reset(): void {
    this.email = '';
    this.name = '';
    this.password = '';
  }
}
