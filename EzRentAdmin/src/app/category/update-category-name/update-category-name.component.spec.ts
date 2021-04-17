import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UpdateCategoryNameComponent } from './update-category-name.component';

describe('UpdateCategoryNameComponent', () => {
  let component: UpdateCategoryNameComponent;
  let fixture: ComponentFixture<UpdateCategoryNameComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ UpdateCategoryNameComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(UpdateCategoryNameComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
