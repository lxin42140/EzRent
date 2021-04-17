import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ViewAllAdminsComponent } from './view-all-admins.component';

describe('ViewAllAdminsComponent', () => {
  let component: ViewAllAdminsComponent;
  let fixture: ComponentFixture<ViewAllAdminsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ViewAllAdminsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ViewAllAdminsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
