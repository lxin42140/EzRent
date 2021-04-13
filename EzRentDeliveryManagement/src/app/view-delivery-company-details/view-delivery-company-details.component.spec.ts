import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ViewDeliveryCompanyDetailsComponent } from './view-delivery-company-details.component';

describe('ViewDeliveryCompanyDetailsComponent', () => {
  let component: ViewDeliveryCompanyDetailsComponent;
  let fixture: ComponentFixture<ViewDeliveryCompanyDetailsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ViewDeliveryCompanyDetailsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ViewDeliveryCompanyDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
