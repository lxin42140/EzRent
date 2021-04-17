import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ViewAllCompletedDeliveriesComponent } from './view-all-completed-deliveries.component';

describe('ViewAllCompletedDeliveriesComponent', () => {
  let component: ViewAllCompletedDeliveriesComponent;
  let fixture: ComponentFixture<ViewAllCompletedDeliveriesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ViewAllCompletedDeliveriesComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ViewAllCompletedDeliveriesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
