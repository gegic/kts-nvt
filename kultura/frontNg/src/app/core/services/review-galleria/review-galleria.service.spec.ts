import { TestBed } from '@angular/core/testing';

import { ReviewGalleriaService } from './review-galleria.service';

describe('ReviewGalleriaService', () => {
  let service: ReviewGalleriaService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ReviewGalleriaService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
