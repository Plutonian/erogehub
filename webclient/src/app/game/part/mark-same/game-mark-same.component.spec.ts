import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GameMarkSameComponent } from './game-mark-same.component';

describe('StateChangeComponent', () => {
  let component: GameMarkSameComponent;
  let fixture: ComponentFixture<GameMarkSameComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ GameMarkSameComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(GameMarkSameComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
