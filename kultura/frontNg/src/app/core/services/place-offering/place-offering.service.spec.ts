import {TestBed} from '@angular/core/testing';

import {PlaceOfferingService} from './place-offering.service';

describe('PlaceOfferingService', () => {
  let service: PlaceOfferingService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PlaceOfferingService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
