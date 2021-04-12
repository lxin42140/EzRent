import { TestBed } from '@angular/core/testing';

import { DeliveryCompanyServiceService } from './delivery-company-service.service';

describe('DeliveryCompanyServiceService', () => {
  let service: DeliveryCompanyServiceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DeliveryCompanyServiceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
