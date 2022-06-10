import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PlaystateChangeComponent } from './playstate-change.component';

describe('PlaystateChangeComponent', () => {
  let component: PlaystateChangeComponent;
  let fixture: ComponentFixture<PlaystateChangeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PlaystateChangeComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PlaystateChangeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
