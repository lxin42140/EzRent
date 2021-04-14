import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ViewAllDeliveryCompaniesComponent } from './view-all-delivery-companies.component';

describe('ViewAllDeliveryCompaniesComponent', () => {
  let component: ViewAllDeliveryCompaniesComponent;
  let fixture: ComponentFixture<ViewAllDeliveryCompaniesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ViewAllDeliveryCompaniesComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ViewAllDeliveryCompaniesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
