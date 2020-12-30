import { TestBed } from '@angular/core/testing';

import { CulturalOfferingDetailsService } from './cultural-offering-details.service';

describe('CulturalOfferingDetailsService', () => {
  let service: CulturalOfferingDetailsService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CulturalOfferingDetailsService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
