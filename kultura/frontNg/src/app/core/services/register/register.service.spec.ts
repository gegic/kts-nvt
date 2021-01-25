import {fakeAsync, getTestBed, TestBed, tick} from '@angular/core/testing';

import {RegisterService} from './register.service';
import {User} from '../../models/user';
import {HttpClientTestingModule, HttpTestingController} from '@angular/common/http/testing';
import {HttpClient, HttpErrorResponse} from '@angular/common/http';
import {UserService} from '../users/users.service';
import {Post} from '../../models/post';

describe('RegisterService', () => {
  let service: RegisterService;
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
    service = TestBed.inject(RegisterService);
  });

  it('should be created', () => {
    expect(service).toBeDefined();
  });

  it('checkExistence()  should verify existence of user e-mail address', fakeAsync(() => {
    const mockResult = 'John Johnny';

    let result;
    service.checkExistence('user@mail.com').subscribe(data => result = data);


    const req = httpMock.expectOne('/auth/exists/email/user@mail.com');
    expect(req.request.method).toBe('GET');
    req.flush(mockResult);


    tick();

    expect(result).toBeDefined();
    expect(result).toBeTruthy();
    expect(result).toEqual(mockResult);
  }));


  it('reset() should reset user fields to default values', () => {
    service.reset();
    expect(service.email).toEqual('');
    expect(service.firstName).toEqual('');
    expect(service.lastName).toEqual('');
    expect(service.password).toEqual('');
  });

  it('getIdByEmail() should return id linked with ent email', fakeAsync(() => {
    let idResult;

    const mockResult = '1';


    service.getIdByMail('user@mail.com').subscribe(data => idResult = data);


    const req = httpMock.expectOne('/auth/exists/email/id/user@mail.com');
    expect(req.request.method).toBe('GET');
    req.flush(mockResult);

    tick();

    expect(idResult).toBeDefined();
    expect(idResult).toEqual('1');
  }));

  it('register() should regiter user', fakeAsync(() => {


      let res;
      service.register().subscribe(data => {
        res = data;
        console.log(res);
      });
      const req = httpMock.expectOne('/auth/register');
      expect(req.request.method).toBe('POST');
      tick();

    }
  ));

  it('should throw error', () => {
    let error: HttpErrorResponse;
    const mockResult = '1';

    service.getIdByMail(mockResult).subscribe(null, e => {
      error = e;
    });
    const req = httpMock.expectOne('/auth/exists/email/id/1');
    expect(req.request.method).toBe('GET');

    req.flush('Something went wrong', {
      status: 404,
      statusText: 'Network error'
    });

    expect(error.statusText).toEqual('Network error');
    expect(error.status).toEqual(404);
  });
});
