import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GroupCvComponent } from './group-cv.component';

describe('GroupCvComponent', () => {
  let component: GroupCvComponent;
  let fixture: ComponentFixture<GroupCvComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ GroupCvComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(GroupCvComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
