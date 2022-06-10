import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EmotionChangeComponent } from './emotion-change.component';

describe('EmotionChangeComponent', () => {
  let component: EmotionChangeComponent;
  let fixture: ComponentFixture<EmotionChangeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ EmotionChangeComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(EmotionChangeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
