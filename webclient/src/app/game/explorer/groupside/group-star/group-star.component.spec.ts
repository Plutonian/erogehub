import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GroupStarComponent } from './group-star.component';

describe('GroupStarComponent', () => {
  let component: GroupStarComponent;
  let fixture: ComponentFixture<GroupStarComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ GroupStarComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(GroupStarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
