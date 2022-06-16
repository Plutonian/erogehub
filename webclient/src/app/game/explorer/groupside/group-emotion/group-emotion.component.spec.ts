import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GroupEmotionComponent } from './group-emotion.component';

describe('GroupEmotionComponent', () => {
  let component: GroupEmotionComponent;
  let fixture: ComponentFixture<GroupEmotionComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ GroupEmotionComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(GroupEmotionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
