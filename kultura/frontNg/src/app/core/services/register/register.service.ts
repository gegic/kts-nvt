import {Injectable} from '@angular/core';
import {User} from '../../models/user';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class RegisterService {

  private user: User;

  constructor(private httpClient: HttpClient) {
    this.user = new User();
  }

  get email(): string | null {
    return this.user.email;
  }

  set email(value: string | null) {
    this.user.email = value;
  }

  get firstName(): string | null {
    return this.user.firstName;
  }

  set firstName(value: string | null) {
    this.user.firstName = value;
  }

  get lastName(): string | null {
    return this.user.lastName;
  }

  set lastName(value: string | null) {
    this.user.lastName = value;
  }

  get password(): string | null {
    return this.user.password;
  }

  set password(value: string | null) {
    this.user.password = value;
  }

  reset(): void {
    this.user = new User();
  }

  checkExistence(email: string): Observable<any> {
    return this.httpClient.get(`/auth/exists/email/${email}`);
  }

  getIdByMail(email: string): Observable<any> {
    return this.httpClient.get(`/auth/exists/email/id/${email}`);
  }

  register(): Observable<any> {
    return this.httpClient.post('/auth/register', this.user);
  }
}
