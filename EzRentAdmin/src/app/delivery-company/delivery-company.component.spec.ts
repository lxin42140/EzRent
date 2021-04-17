import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DeliveryCompanyComponent } from './delivery-company.component';

describe('DeliveryCompanyComponent', () => {
  let component: DeliveryCompanyComponent;
  let fixture: ComponentFixture<DeliveryCompanyComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DeliveryCompanyComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(DeliveryCompanyComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
