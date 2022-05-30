import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GroupsideComponent } from './groupside.component';

describe('GroupsideComponent', () => {
  let component: GroupsideComponent;
  let fixture: ComponentFixture<GroupsideComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ GroupsideComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(GroupsideComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
