import { TestBed } from '@angular/core/testing';

import { DeliveryCompanyService } from './delivery-company.service';

describe('DeliveryCompanyService', () => {
  let service: DeliveryCompanyService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DeliveryCompanyService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
