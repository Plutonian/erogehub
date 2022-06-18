import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GroupLocationComponent } from './group-location.component';

describe('GroupLocationComponent', () => {
  let component: GroupLocationComponent;
  let fixture: ComponentFixture<GroupLocationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ GroupLocationComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(GroupLocationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
