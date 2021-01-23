import {getTestBed, TestBed} from '@angular/core/testing';

import {AuthService} from './auth.service';
import {User} from '../../models/user';
import {Authority} from '../../models/authority';
import {HttpClientTestingModule, HttpTestingController} from '@angular/common/http/testing';
import {MapService} from '../map/map.service';
import {HttpClient} from '@angular/common/http';
import {Router} from '@angular/router';
import {RouterTestingModule} from '@angular/router/testing';

describe('AuthService', () => {
  let service: AuthService;
  let httpMock: HttpTestingController;
  let httpClient: HttpClient;
  let injector;


  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule],
      providers: [AuthService]
    });
    service = TestBed.inject(AuthService);
    injector = getTestBed();
    httpClient = TestBed.inject(HttpClient);
    httpMock = TestBed.inject(HttpTestingController);
  });

  it('should be created', () => {
    expect(service).toBeDefined();
  });

  it('should log a user in', () => {
    const user = getUser();

    service.login('some-token', user);

    expect(service.user.getValue()).toEqual(user);
    expect(service.token.getValue()).toEqual('some-token');

    expect(localStorage.getItem('user')).toEqual(JSON.stringify(user));
    expect(localStorage.getItem('token')).toEqual('some-token');

    logout(service);
  });

  it('should log the user out', () => {
    const user = getUser();
    login(service, user, 'some-token');

    service.logout();

    expect(service.user.getValue()).toBeNull();
    expect(service.token.getValue()).toBeNull();
    expect(localStorage.getItem('user')).toBeNull();
    expect(localStorage.getItem('token')).toBeNull();
  });

  it('should check if the user is logged in and return true', () => {
    const user = getUser();
    login(service, user, 'some-token');

    expect(service.isLoggedIn()).toBeTrue();

    logout(service);
  });
  it('should check if the user is logged in and return false', () => {

    expect(service.isLoggedIn()).toBeFalse();

  });

  it('should return user\'s role', () => {
    const user = getUser();
    login(service, user, 'some-token');
    const role = service.getUserRole();
    expect(role).toEqual('USER');

    logout(service);
  });

  it('should update user data', () => {
    const user = getUser();
    login(service, user, 'some-token');
    user.id = 2;
    user.firstName = 'Someoneelse';
    user.email = 'someone@mail.com';
    const role = service.updateUserData(user);

    expect(service.user.getValue()).toEqual(user);
    expect(service.token.getValue()).toEqual('some-token');

    expect(localStorage.getItem('user')).toEqual(JSON.stringify(user));
    expect(localStorage.getItem('token')).toEqual('some-token');

    logout(service);
  });
});

function getUser(): User {
  const authUser: Authority = {
    id: 3,
    authority: 'ROLE_USER'
  };
  const user: User = new User();
  user.id = 1;
  user.email = 'email@mail.com';
  user.firstName = 'Korisnik';
  user.lastName = 'Korisnikovic';
  user.verified = true;
  user.authorities = [authUser];

  return user;
}

function login(service: AuthService, user: User, token: string): void {
  service.token.next(token);
  service.user.next(user);
  localStorage.setItem('user', JSON.stringify(user));
  localStorage.setItem('token', token);
}

function logout(service: AuthService): void {
  service.token.next(null);
  service.user.next(null);
  localStorage.removeItem('user');
  localStorage.removeItem('token');
}
