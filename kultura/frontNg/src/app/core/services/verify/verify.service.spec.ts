import {fakeAsync, getTestBed, TestBed, tick} from '@angular/core/testing';

import {VerifyService} from './verify.service';
import {HttpClientTestingModule, HttpTestingController} from '@angular/common/http/testing';
import {HttpClient, HttpErrorResponse} from '@angular/common/http';
import {User} from '../../models/user';

describe('VerifyService', () => {
  let service: VerifyService;
  let injector;
  let httpMock: HttpTestingController;
  let httpClient: HttpClient;


  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [VerifyService]
    });

    injector = getTestBed();
    httpClient = TestBed.inject(HttpClient);
    httpMock = TestBed.inject(HttpTestingController);
    service = TestBed.inject(VerifyService);
  });

  it('should be created', () => {
    expect(service).toBeDefined();
  });

  it('verify() should query url and verify user', fakeAsync(() => {
    let updatedUser: User = new User();
    updatedUser.firstName = 'Firstname';
    updatedUser.lastName = 'NewLastname';
    updatedUser.email = 'test4@mail.com';
    updatedUser.id = 1;
    updatedUser.verified = false;

    const mockUser: User = new User();
    mockUser.firstName = 'Firstname';
    mockUser.lastName = 'Lastname';
    mockUser.email = 'test4@mail.com';
    mockUser.id = 1;
    mockUser.verified = true;

    service.verify('1').subscribe(data => updatedUser = data);


    const req = httpMock.expectOne('/auth/verify/1');
    expect(req.request.method).toBe('GET');
    req.flush(mockUser);

    tick();

    expect(updatedUser).toBeDefined();
    expect(updatedUser.id).toEqual(1);
    expect(updatedUser.firstName).toEqual('Firstname');
    expect(updatedUser.lastName).toEqual('Lastname');
    expect(updatedUser.email).toEqual('test4@mail.com');
    expect(updatedUser.verified).toEqual(true);
  }));

  it('checkExistence() should query url and check is user verified', fakeAsync(() => {
    let verified: boolean;
    const mockVerified = true;
    service.checkExistence('1').subscribe(res => {
      verified = res;
    });

    const req = httpMock.expectOne(`/auth/exists/verify/id/1`);
    expect(req.request.method).toBe('GET');
    req.flush(mockVerified);
    expect(verified).toEqual(true);

  }));

  it('should throw error', () => {
    let error: HttpErrorResponse;

    service.verify('1').subscribe(null, e => {
      error = e;
    });

    const req = httpMock.expectOne('/auth/verify/1');
    expect(req.request.method).toBe('GET');

    req.flush('Something went wrong', {
      status: 404,
      statusText: 'Not Found'
    });

    expect(error.statusText).toEqual('Not Found');
    expect(error.status).toEqual(404);
  });
});
