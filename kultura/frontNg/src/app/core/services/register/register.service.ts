import { Injectable } from '@angular/core';
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

  reset(): void {
    this.user = new User();
  }

  checkExistence(email: string): Observable<any> {
    return this.httpClient.get(`/api/auth/exists/email/${email}`);
  }

  register(): Observable<any> {
    return this.httpClient.post('/api/auth/register', this.user);
  }

  get email(): string {
    return this.user.email;
  }

  set email(value: string) {
    this.user.email = value;
  }

  get firstName(): string {
    return this.user.firstName;
  }

  set firstName(value: string) {
    this.user.firstName = value;
  }

  get lastName(): string {
    return this.user.lastName;
  }

  set lastName(value: string) {
    this.user.lastName = value;
  }

  get password(): string {
    return this.user.password;
  }

  set password(value: string) {
    this.user.password = value;
  }
}
