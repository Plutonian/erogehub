import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GroupDateComponent } from './group-date.component';

describe('GroupDateComponent', () => {
  let component: GroupDateComponent;
  let fixture: ComponentFixture<GroupDateComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ GroupDateComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(GroupDateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
