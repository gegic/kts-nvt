import {fakeAsync, getTestBed, TestBed, tick} from '@angular/core/testing';

import {PhotoService} from './photo.service';
import {HttpClientTestingModule, HttpTestingController} from '@angular/common/http/testing';
import {CulturalOfferingDetailsService} from '../cultural-offering-details/cultural-offering-details.service';
import {HttpClient, HttpErrorResponse} from '@angular/common/http';
import {CulturalOfferingPhoto} from '../../models/culturalOfferingPhoto';
import {Form} from '@angular/forms';

describe('PhotoService', () => {
  let service: PhotoService;
  let httpMock: HttpTestingController;
  let httpClient: HttpClient;
  let injector;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [CulturalOfferingDetailsService]
    });
    service = TestBed.inject(PhotoService);
    injector = getTestBed();
    httpClient = TestBed.inject(HttpClient);
    httpMock = TestBed.inject(HttpTestingController);
  });

  it('should be created', () => {
    expect(service).toBeDefined();
  });

  it('should retrieve photos', fakeAsync(() => {
    let photos: CulturalOfferingPhoto[] = [];
    const mockPhotos: CulturalOfferingPhoto[] = getPhotos();
    service.getPhotos(1, 1).subscribe(val => {
      photos = val;
    });
    const req = httpMock.expectOne('/api/photos/cultural-offering/1?page=1');
    expect(req.request.method).toBe('GET');
    req.flush(mockPhotos.filter(p => p.culturalOfferingId === 1));

    tick();
    // (\w+): ([\d'\w.]+),?
    expect(photos.length).toEqual(2);
    expect(photos[0].id).toEqual(1);
    expect(photos[0].culturalOfferingId).toEqual(1);
    expect(photos[0].height).toEqual(700);
    expect(photos[0].width).toEqual(800);
    expect(photos[0].hovering).toEqual(false);

    expect(photos[1].id).toEqual(3);
    expect(photos[1].culturalOfferingId).toEqual(1);
    expect(photos[1].height).toEqual(700);
    expect(photos[1].width).toEqual(800);
    expect(photos[1].hovering).toEqual(false);

  }));

  it('should call photo adding endpoint', () => {
    const blob = new Blob([''], { type: 'text/html' });
    blob[`lastModifiedDate`] = '';
    blob[`name`] = 'photo';
    const fakeFile = blob as File;

    service.addPhoto(fakeFile, 1).subscribe();

    const req = httpMock.expectOne('/api/photos/cultural-offering/1');
    expect(req.request.method).toEqual('POST');
    expect(((req.request.body as FormData).get('photo') as File)).toBeDefined();
  });

  it('should call photo removal endpoint', () => {

    service.delete(1).subscribe();

    const req = httpMock.expectOne('/api/photos/1');
    expect(req.request.method).toEqual('DELETE');
  });

  it('should throw error', () => {
    let error: HttpErrorResponse;

    service.delete(1).subscribe(null, e => {
      error = e;
    });
    const req = httpMock
      .expectOne('/api/photos/1');
    expect(req.request.method).toBe('DELETE');

    req.flush('Something went wrong', {
      status: 404,
      statusText: 'Network error'
    });

    expect(error.statusText).toEqual('Network error');
    expect(error.status).toEqual(404);
  });
});

function getPhotos(): CulturalOfferingPhoto[] {
  return [
    {id: 1, culturalOfferingId: 1, height: 700, width: 800, hovering: false},
    {id: 2, culturalOfferingId: 2, height: 700, width: 800, hovering: false},
    {id: 3, culturalOfferingId: 1, height: 700, width: 800, hovering: false}
  ];
}
