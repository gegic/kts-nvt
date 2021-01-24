import {TestBed} from '@angular/core/testing';

import {JwtInterceptor} from './jwt.interceptor';
import {AuthService} from '../services/auth/auth.service';
import createSpy = jasmine.createSpy;
import {HttpHandler, HttpRequest} from '@angular/common/http';
import {User} from '../models/user';
import {Authority} from '../models/authority';
import {HttpClientTestingModule} from '@angular/common/http/testing';
import {RouterTestingModule} from '@angular/router/testing';

describe('JwtInterceptor', () => {

  let authService: AuthService;
  let interceptor: JwtInterceptor;
  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule],
      providers: [JwtInterceptor, AuthService]
    });
    authService = TestBed.inject(AuthService);
    interceptor = TestBed.inject(JwtInterceptor);
  });

  it('should be created', () => {
    expect(interceptor).toBeTruthy();
  });

  it('should intercept requests and add bearer before each', () => {
    const user: User = new User();
    const a = new Authority();
    a.authority = 'ROLE_USER';
    a.id = 1;
    user.id = 1;
    user.email = 'email@mail.com';
    user.password = 'admin123';
    user.firstName = 'EmailAdmin';
    user.verified = true;
    user.lastName = 'Adminic';
    user.authorities = [a];
    const token = 'eyJhbGciOiJIUzUxMiJ9.eyJleHAiOjY0MDYzMjY3MjAwLCJpYXQiOj' +
      'E2MTE0MzEwODAsInVzZXJuYW1lIjoiZW1haWxAbWFpbC5jb20ifQ.hGdnT6dK_zjidEYzLSWy0Y8ihthq' +
      'IsDjbIfLmdVVR3_ZQCj26VnBdOFUyAHe-tJRc0TUgbf3Dg14Z-_EVIlzwA';
    authService.user.next(user);
    authService.token.next(token);

    const request = new HttpRequest('GET', '/api/some-request');
    let header = '';
    const next = {handle: createSpy('handle').and.callFake((req: HttpRequest<any>) => {
      header = req.headers.get('Authorization');
    })};
    interceptor.intercept(request, next);

    expect(next.handle).toHaveBeenCalled();
    expect(header).toEqual('Bearer ' + token);

  });
});
