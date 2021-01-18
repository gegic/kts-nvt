import {Injectable} from '@angular/core';
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

  login(token: string, userData: User): void {
    const user: User = Object.assign(new User(), userData);
    this.user.next(user);
    this.token.next(token);
    localStorage.setItem('user', JSON.stringify(user));
    localStorage.setItem('token', token);
  }

  logout(): void {
    // remove user from local storage and set current user to null
    this.user.next(null);
    this.token.next(null);
    localStorage.removeItem('user');
    localStorage.removeItem('token');
    this.router.navigateByUrl('/login');
  }

  getUserRole(): string {
    const user = Object.assign(new User(), this.user.getValue());
    return user.getRole();
  }

  isLoggedIn(): boolean {
    return !!this.user.getValue();
  }
  
  updateUserData(user: User): void{
    this.user.next(user);
    localStorage.setItem('user', JSON.stringify(user));
  }
}
