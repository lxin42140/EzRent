import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreateNewTagComponent } from './create-new-tag.component';

describe('CreateNewTagComponent', () => {
  let component: CreateNewTagComponent;
  let fixture: ComponentFixture<CreateNewTagComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CreateNewTagComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CreateNewTagComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
