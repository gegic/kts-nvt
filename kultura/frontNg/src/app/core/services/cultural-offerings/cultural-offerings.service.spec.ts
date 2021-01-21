import {TestBed} from '@angular/core/testing';

import {CulturalOfferingsService} from './cultural-offerings.service';

describe('CulturalOfferingsService', () => {
  let service: CulturalOfferingsService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CulturalOfferingsService);
  });

  it('should be created', () => {
    expect(service).toBeDefined();
  });
});
