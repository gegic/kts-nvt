import {fakeAsync, getTestBed, TestBed, tick} from '@angular/core/testing';

import {UserService} from './users.service';
import {HttpClientTestingModule, HttpTestingController} from '@angular/common/http/testing';
import {HttpClient, HttpErrorResponse} from '@angular/common/http';
import {User} from '../../models/user';

describe('UserServiceService', () => {
  let service: UserService;
  let injector;
  let httpMock: HttpTestingController;
  let httpClient: HttpClient;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [UserService]
    });

    injector = getTestBed();
    httpClient = TestBed.inject(HttpClient);
    httpMock = TestBed.inject(HttpTestingController);
    service = TestBed.inject(UserService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('getById()  should query url and get a user', fakeAsync(() => {
    let user: User = new User();
    user.firstName = 'Firstname';
    user.lastName = 'Lastname';
    user.email = 'test4@mail.com';
    user.id = 1;

    const mockUser: User = new User();
    mockUser.firstName = 'Firstname';
    mockUser.lastName = 'Lastname';
    mockUser.email = 'test4@mail.com';
    mockUser.id = 1;

    service.getById(1).subscribe(data => user = data);


    const req = httpMock.expectOne('/api/users/1');
    expect(req.request.method).toBe('GET');
    req.flush(mockUser);

    tick();

    expect(user).toBeDefined();
    expect(user.id).toEqual(1);
    expect(user.firstName).toEqual('Firstname');
    expect(user.lastName).toEqual('Lastname');
    expect(user.email).toEqual('test4@mail.com');
  }));

  it('update()  should query url and updated a user', fakeAsync(() => {
    let updatedUser: User = new User();
    updatedUser.firstName = 'Firstname';
    updatedUser.lastName = 'Lastname';
    updatedUser.email = 'test4@mail.com';
    updatedUser.id = 1;

    const mockUser: User = new User();
    mockUser.firstName = 'Firstname';
    mockUser.lastName = 'NewLastname';
    mockUser.email = 'test4@mail.com';
    mockUser.id = 1;

    service.getById(1).subscribe(data => updatedUser = data);


    const req = httpMock.expectOne('/api/users/1');
    expect(req.request.method).toBe('GET');
    req.flush(mockUser);

    tick();

    expect(updatedUser).toBeDefined();
    expect(updatedUser.id).toEqual(1);
    expect(updatedUser.firstName).toEqual('Firstname');
    expect(updatedUser.lastName).toEqual('NewLastname');
    expect(updatedUser.email).toEqual('test4@mail.com');
  }));

  it('should throw error', () => {
    let error: HttpErrorResponse;
    const mockUser: User = new User();
    mockUser.firstName = 'Firstname';
    mockUser.lastName = 'Lastname';
    mockUser.email = 'test4@mail.com';
    mockUser.id = 1;

    service.update(mockUser).subscribe(null, e => {
      error = e;
    });
    const req = httpMock.expectOne('/api/users');
    expect(req.request.method).toBe('PUT');

    req.flush('Something went wrong', {
      status: 404,
      statusText: 'Network error'
    });

    expect(error.statusText).toEqual('Network error');
    expect(error.status).toEqual(404);
  });
});
