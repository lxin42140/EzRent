import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ViewAllOngoingDeliveriesComponent } from './view-all-ongoing-deliveries.component';

describe('ViewAllOngoingDeliveriesComponent', () => {
  let component: ViewAllOngoingDeliveriesComponent;
  let fixture: ComponentFixture<ViewAllOngoingDeliveriesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ViewAllOngoingDeliveriesComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ViewAllOngoingDeliveriesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
