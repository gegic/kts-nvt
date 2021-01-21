import {fakeAsync, getTestBed, TestBed, tick} from '@angular/core/testing';

import {ModeratorService} from './moderator.service';
import {HttpClientTestingModule, HttpTestingController} from '@angular/common/http/testing';
import {HttpClient} from '@angular/common/http';
import {Moderator} from '../../models/moderator';

describe('ModeratorService', () => {
  let service: ModeratorService;
  let injector;
  let httpMock: HttpTestingController;
  let httpClient: HttpClient;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [ModeratorService]
    });

    injector = getTestBed();
    httpClient = TestBed.inject(HttpClient);
    httpMock = TestBed.inject(HttpTestingController);
    service = TestBed.inject(ModeratorService);
  });

  it('should be created', () => {
    expect(service).toBeDefined();
  });

  it('getModerators() should query url and get all moderators', fakeAsync(() => {
    let moderators: Moderator[] = [];
    const mockModerators: Moderator[] = [
      {
        id: '1',
        email: 'test1@mail.com',
        firstName: 'Firstname1',
        lastName: 'Lastname1',
      },
      {
        id: '2',
        email: 'test2@mail.com',
        firstName: 'Firstname2',
        lastName: 'Lastname2',
      },
      {
        id: '3',
        email: 'test3@mail.com',
        firstName: 'Firstname3',
        lastName: 'Lastname3',
      },
      {
        id: '4',
        email: 'test4@mail.com',
        firstName: 'Firstname4',
        lastName: 'Lastname4',
      }];

    service.getModerators(1).subscribe(data => {
      moderators = data;
    });

    const req = httpMock.expectOne('/api/users/moderators?page=1');
    expect(req.request.method).toBe('GET');
    req.flush(mockModerators);

    tick();

    expect(moderators.length).toEqual(4, 'should contain given amount of moderators');

    expect(moderators[0].id).toEqual('1');
    expect(moderators[0].email).toEqual('test1@mail.com');
    expect(moderators[0].firstName).toEqual('Firstname1');
    expect(moderators[0].lastName).toEqual('Lastname1');

    expect(moderators[1].id).toEqual('2');
    expect(moderators[1].email).toEqual('test2@mail.com');
    expect(moderators[1].firstName).toEqual('Firstname2');
    expect(moderators[1].lastName).toEqual('Lastname2');

    expect(moderators[2].id).toEqual('3');
    expect(moderators[2].email).toEqual('test3@mail.com');
    expect(moderators[2].firstName).toEqual('Firstname3');
    expect(moderators[2].lastName).toEqual('Lastname3');

    expect(moderators[3].id).toEqual('4');
    expect(moderators[3].email).toEqual('test4@mail.com');
    expect(moderators[3].firstName).toEqual('Firstname4');
    expect(moderators[3].lastName).toEqual('Lastname4');

  }));

  it('createModerator()  should query url and save a moderator', fakeAsync(() => {
    let newModerator: Moderator = {
      email: 'test4@mail.com',
      firstName: 'Firstname',
      lastName: 'Lastname',
    };

    const mockModerator: Moderator =
      {
        id: '1',
        email: 'test4@mail.com',
        firstName: 'Firstname',
        lastName: 'Lastname',
        password: 'Admin123'
      };

    service.createModerator(newModerator).subscribe(data => newModerator = data);


    const req = httpMock.expectOne('/api/users/moderator');
    expect(req.request.method).toBe('POST');
    req.flush(mockModerator);

    tick();

    expect(newModerator).toBeDefined();
    expect(newModerator.id).toEqual('1');
    expect(newModerator.firstName).toEqual('Firstname');
    expect(newModerator.lastName).toEqual('Lastname');
    expect(newModerator.email).toEqual('test4@mail.com');
  }));

  it('getModeratorById()  should query url and save a moderator', fakeAsync(() => {
    let newModerator: Moderator = {
      email: 'test4@mail.com',
      firstName: 'Firstname4',
      lastName: 'Lastname4',
    };

    const mockModerator: Moderator =
      {
        id: '1',
        email: 'test4@mail.com',
        firstName: 'Firstname4',
        lastName: 'Lastname4',
        password: 'Admin123'
      };

    service.getModeratorById(2).subscribe(data => newModerator = data);


    const req = httpMock.expectOne('/api/users/2');
    expect(req.request.method).toBe('GET');
    req.flush(mockModerator);

    tick();

    expect(newModerator).toBeDefined();
    expect(newModerator.id).toEqual('1');
    expect(newModerator.firstName).toEqual('Firstname4');
    expect(newModerator.lastName).toEqual('Lastname4');
    expect(newModerator.email).toEqual('test4@mail.com');
  }));

  it('update() should query url and edit a moderator', fakeAsync(() => {
    let updateModerator: Moderator = {
      email: 'test4@mail.com',
      firstName: 'Firstname',
      lastName: 'Lastname4',
    };


    const mockModerator: Moderator =
      {
        id: '1',
        email: 'test4@mail.com',
        firstName: 'Firstname4',
        lastName: 'Lastname4',
        password: 'Admin123'
      };

    service.updateModerator(updateModerator).subscribe(res => updateModerator = res
    );

    const req = httpMock.expectOne('/api/users');
    expect(req.request.method).toBe('PUT');
    req.flush(mockModerator);

    tick();
    expect(updateModerator).toBeDefined();
    expect(updateModerator.id).toEqual('1');
    expect(updateModerator.firstName).toEqual('Firstname4');
    expect(updateModerator.lastName).toEqual('Lastname4');
    expect(updateModerator.email).toEqual('test4@mail.com');
  }));

  it('delete() should query url and delete a moderator', () => {
    service.delete(1).subscribe(res => {
    });

    const req = httpMock.expectOne(`/api/users/1`);
    expect(req.request.method).toBe('DELETE');
    req.flush({});
  });

});
