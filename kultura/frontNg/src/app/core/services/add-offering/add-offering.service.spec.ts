import {TestBed} from '@angular/core/testing';

import {AddOfferingService} from './add-offering.service';

describe('AddOfferingService', () => {
  let service: AddOfferingService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AddOfferingService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
