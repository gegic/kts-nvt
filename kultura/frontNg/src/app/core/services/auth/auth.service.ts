import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {User} from '../../models/user';
import {BehaviorSubject} from 'rxjs';
import {Router} from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  user: BehaviorSubject<User | null>;
  token: BehaviorSubject<string | null>;

  constructor(private httpClient: HttpClient,
              private router: Router) {
    this.user = new BehaviorSubject<User | null>(JSON.parse(localStorage.getItem('user') as string));
    this.token = new BehaviorSubject<string | null>(localStorage.getItem('token') as string);
  }

  login(token: string, user: User): void {
    localStorage.setItem('user', JSON.stringify(user));
    localStorage.setItem('token', token);
    this.user.next(user);
    this.token.next(token);
  }

  logout(): void {
    // remove user from local storage and set current user to null
    localStorage.removeItem('user');
    localStorage.removeItem('token');
    this.user.next(null);
    this.token.next(null);
    this.router.navigateByUrl('/api/login');
  }
}
