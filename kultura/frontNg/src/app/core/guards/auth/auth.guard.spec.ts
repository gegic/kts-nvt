import {getTestBed, TestBed} from '@angular/core/testing';

import {AuthGuard} from './auth.guard';
import {Router} from '@angular/router';
import {AuthService} from '../../services/auth/auth.service';
import {HttpClientTestingModule} from '@angular/common/http/testing';
import {User} from '../../models/user';
import {Authority} from '../../models/authority';

describe('AuthGuard', () => {
  let injector: TestBed;
  let guard: AuthGuard;
  let authService: AuthService;
  const router = {
    navigate: jasmine.createSpy('navigate')
  };

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [AuthGuard, AuthService, {provide: Router, useValue: router}]
    });
    injector = getTestBed();
    guard = TestBed.inject(AuthGuard);
    authService = TestBed.inject(AuthService);
  });

  it('should be created', () => {
    expect(guard).toBeTruthy();
  });

  it('should check if user is with role user and return true', () => {
    const routeMock: any = {snapshot: {}, data: {roles: ['MODERATOR', 'USER', 'UNREGISTERED']}};
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
    authService.user.next(user);
    authService.token.next('eyJhbGciOiJIUzUxMiJ9.eyJleHAiOjY0MDYzMjY3MjAwLCJpYXQiOj' +
      'E2MTE0MzEwODAsInVzZXJuYW1lIjoiZW1haWxAbWFpbC5jb20ifQ.hGdnT6dK_zjidEYzLSWy0Y8ihthq' +
      'IsDjbIfLmdVVR3_ZQCj26VnBdOFUyAHe-tJRc0TUgbf3Dg14Z-_EVIlzwA');
    expect(guard.canActivate(routeMock, null)).toBeTrue();

    authService.user.next(null);
    authService.token.next(null);
  });

  it('should check if user is with role user and redirect them and return false', () => {
    const routeMock: any = {snapshot: {}, data: {roles: ['MODERATOR']}};
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
    authService.user.next(user);
    authService.token.next('eyJhbGciOiJIUzUxMiJ9.eyJleHAiOjY0MDYzMjY3MjAwLCJpYXQiOj' +
      'E2MTE0MzEwODAsInVzZXJuYW1lIjoiZW1haWxAbWFpbC5jb20ifQ.hGdnT6dK_zjidEYzLSWy0Y8ihthq' +
      'IsDjbIfLmdVVR3_ZQCj26VnBdOFUyAHe-tJRc0TUgbf3Dg14Z-_EVIlzwA');
    expect(guard.canActivate(routeMock, null)).toBeFalse();

    expect(router.navigate).toHaveBeenCalledWith(['']);
    authService.user.next(null);
    authService.token.next(null);
  });

  it('should check if user is with role moderator and return true', () => {
    const routeMock: any = {snapshot: {}, data: {roles: ['MODERATOR']}};
    const user: User = new User();
    const a = new Authority();
    a.authority = 'ROLE_MODERATOR';
    a.id = 1;
    user.id = 1;
    user.email = 'email@mail.com';
    user.password = 'admin123';
    user.firstName = 'EmailAdmin';
    user.verified = true;
    user.lastName = 'Adminic';
    user.authorities = [a];
    authService.user.next(user);
    authService.token.next('eyJhbGciOiJIUzUxMiJ9.eyJleHAiOjY0MDYzMjY3MjAwLCJpYXQiOj' +
      'E2MTE0MzEwODAsInVzZXJuYW1lIjoiZW1haWxAbWFpbC5jb20ifQ.hGdnT6dK_zjidEYzLSWy0Y8ihthq' +
      'IsDjbIfLmdVVR3_ZQCj26VnBdOFUyAHe-tJRc0TUgbf3Dg14Z-_EVIlzwA');
    expect(guard.canActivate(routeMock, null)).toBeTrue();
    authService.user.next(null);
    authService.token.next(null);
  });

  it('should check if user is with role moderator and return false', () => {
    const routeMock: any = {snapshot: {}, data: {roles: ['ADMIN']}};
    const user: User = new User();
    const a = new Authority();
    a.authority = 'ROLE_MODERATOR';
    a.id = 1;
    user.id = 1;
    user.email = 'email@mail.com';
    user.password = 'admin123';
    user.firstName = 'EmailAdmin';
    user.verified = true;
    user.lastName = 'Adminic';
    user.authorities = [a];
    authService.user.next(user);
    authService.token.next('eyJhbGciOiJIUzUxMiJ9.eyJleHAiOjY0MDYzMjY3MjAwLCJpYXQiOj' +
      'E2MTE0MzEwODAsInVzZXJuYW1lIjoiZW1haWxAbWFpbC5jb20ifQ.hGdnT6dK_zjidEYzLSWy0Y8ihthq' +
      'IsDjbIfLmdVVR3_ZQCj26VnBdOFUyAHe-tJRc0TUgbf3Dg14Z-_EVIlzwA');
    expect(guard.canActivate(routeMock, null)).toBeFalse();

    expect(router.navigate).toHaveBeenCalledWith(['']);    authService.user.next(null);
    authService.token.next(null);
  });

  it('should check if user is with role ADMIN and return true', () => {
    const routeMock: any = {snapshot: {}, data: {roles: ['ADMIN']}};
    const user: User = new User();
    const a = new Authority();
    a.authority = 'ROLE_ADMIN';
    a.id = 1;
    user.id = 1;
    user.email = 'email@mail.com';
    user.password = 'admin123';
    user.firstName = 'EmailAdmin';
    user.verified = true;
    user.lastName = 'Adminic';
    user.authorities = [a];
    authService.user.next(user);
    authService.token.next('eyJhbGciOiJIUzUxMiJ9.eyJleHAiOjY0MDYzMjY3MjAwLCJpYXQiOj' +
      'E2MTE0MzEwODAsInVzZXJuYW1lIjoiZW1haWxAbWFpbC5jb20ifQ.hGdnT6dK_zjidEYzLSWy0Y8ihthq' +
      'IsDjbIfLmdVVR3_ZQCj26VnBdOFUyAHe-tJRc0TUgbf3Dg14Z-_EVIlzwA');
    expect(guard.canActivate(routeMock, null)).toBeTrue();
    authService.user.next(null);
    authService.token.next(null);
  });


  it('should check if user is with role ADMIN and return FALSE', () => {
    const routeMock: any = {snapshot: {}, data: {roles: ['USER']}};
    const user: User = new User();
    const a = new Authority();
    a.authority = 'ROLE_ADMIN';
    a.id = 1;
    user.id = 1;
    user.email = 'email@mail.com';
    user.password = 'admin123';
    user.firstName = 'EmailAdmin';
    user.verified = true;
    user.lastName = 'Adminic';
    user.authorities = [a];
    authService.user.next(user);
    authService.token.next('eyJhbGciOiJIUzUxMiJ9.eyJleHAiOjY0MDYzMjY3MjAwLCJpYXQiOj' +
      'E2MTE0MzEwODAsInVzZXJuYW1lIjoiZW1haWxAbWFpbC5jb20ifQ.hGdnT6dK_zjidEYzLSWy0Y8ihthq' +
      'IsDjbIfLmdVVR3_ZQCj26VnBdOFUyAHe-tJRc0TUgbf3Dg14Z-_EVIlzwA');
    expect(guard.canActivate(routeMock, null)).toBeFalse();

    expect(router.navigate).toHaveBeenCalledWith(['moderators']);
    authService.user.next(null);
    authService.token.next(null);
  });

  it('should check if user is not logged in and return TRUE', () => {
    const routeMock: any = {snapshot: {}, data: {roles: ['UNREGISTERED']}};
    expect(guard.canActivate(routeMock, null)).toBeTrue();
  });

  it('should check if user is not logged in and return false', () => {
    const routeMock: any = {snapshot: {}, data: {roles: ['USER']}};
    expect(guard.canActivate(routeMock, null)).toBeFalse();
    expect(router.navigate).toHaveBeenCalledWith(['login']);
  });


});
