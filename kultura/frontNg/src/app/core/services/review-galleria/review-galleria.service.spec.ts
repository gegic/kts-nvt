import {getTestBed, TestBed} from '@angular/core/testing';

import {ReviewGalleriaService} from './review-galleria.service';
import {HttpClientTestingModule, HttpTestingController} from '@angular/common/http/testing';
import {HttpClient} from '@angular/common/http';

describe('ReviewGalleriaService', () => {
  let service: ReviewGalleriaService;
  let injector;
  let httpMock: HttpTestingController;
  let httpClient: HttpClient;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [ReviewGalleriaService]
    });
    service = TestBed.inject(ReviewGalleriaService);
    injector = getTestBed();
    httpClient = TestBed.inject(HttpClient);
    httpMock = TestBed.inject(HttpTestingController);
  });

  it('should be created', () => {
    expect(service).toBeDefined();
  });
});
