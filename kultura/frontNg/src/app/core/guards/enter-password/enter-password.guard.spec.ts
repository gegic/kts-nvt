import { TestBed } from '@angular/core/testing';

import { EnterPasswordGuard } from './enter-password.guard';

describe('EnterPasswordGuard', () => {
  let guard: EnterPasswordGuard;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    guard = TestBed.inject(EnterPasswordGuard);
  });

  it('should be created', () => {
    expect(guard).toBeTruthy();
  });
});
