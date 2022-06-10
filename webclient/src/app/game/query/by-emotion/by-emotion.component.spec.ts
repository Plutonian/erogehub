import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ByEmotionComponent } from './by-emotion.component';

describe('ByEmotionComponent', () => {
  let component: ByEmotionComponent;
  let fixture: ComponentFixture<ByEmotionComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ByEmotionComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ByEmotionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
