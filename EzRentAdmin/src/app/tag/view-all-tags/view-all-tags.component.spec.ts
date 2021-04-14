import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ViewAllTagsComponent } from './view-all-tags.component';

describe('ViewAllTagsComponent', () => {
  let component: ViewAllTagsComponent;
  let fixture: ComponentFixture<ViewAllTagsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ViewAllTagsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ViewAllTagsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
