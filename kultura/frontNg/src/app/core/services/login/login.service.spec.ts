import {fakeAsync, getTestBed, TestBed, tick} from '@angular/core/testing';

import {LoginService} from './login.service';
import {HttpClientTestingModule, HttpTestingController} from '@angular/common/http/testing';
import {HttpClient} from '@angular/common/http';
import {PlaceOfferingService} from '../place-offering/place-offering.service';
import {User} from '../../models/user';

describe('LoginService', () => {
  let service: LoginService;
  let httpMock: HttpTestingController;
  let httpClient: HttpClient;
  let injector;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [LoginService]
    });
    service = TestBed.inject(LoginService);
    injector = getTestBed();
    httpClient = TestBed.inject(HttpClient);
    httpMock = TestBed.inject(HttpTestingController);
  });

  it('should be created', () => {
    expect(service).toBeDefined();
  });

  it('should log in', fakeAsync(() => {

    service.email = 'email@mail.com';
    service.password = 'admin123';

    const user: User = new User();
    user.id = 1;
    user.email = 'email@mail.com';
    user.password = 'admin123';
    user.firstName = 'EmailAdmin';
    user.verified = true;
    user.lastName = 'Adminic';

    let loggedUser: User;
    service.login().subscribe(val => {
      loggedUser = val;
    });

    const req = httpMock.expectOne('/auth/login');
    expect(req.request.method).toEqual('POST');
    const {body} = req.request;
    expect(body.email).toEqual('email@mail.com');
    expect(body.password).toEqual('admin123');

    tick();

    expect(user.id).toEqual(1);
    expect(user.email).toEqual('email@mail.com');
    expect(user.password).toEqual('admin123');
    expect(user.firstName).toEqual('EmailAdmin');
    expect(user.verified).toEqual(true);
    expect(user.lastName).toEqual('Adminic');

    localStorage.removeItem('user');

    service.email = '';
    service.password = '';
  }));

  it('should send a request to api endpoint and check for user existence', fakeAsync(() => {
    let name = '';
    service.checkExistence('email@mail.com').subscribe(val => {
      name = val;
    });

    const req = httpMock.expectOne('/auth/exists/email/email@mail.com');
    expect(req.request.method).toEqual('GET');
    req.flush('Someuser');

    tick();

    expect(name).toEqual('Someuser');
  }));

  it('should reset the data already present in the service', () => {
    service.email = 'somemail';
    service.password = 'somepassword';
    service.name = 'somename';

    service.reset();

    expect(service.email).toEqual('');
    expect(service.password).toEqual('');
    expect(service.name).toEqual('');
  });
});
