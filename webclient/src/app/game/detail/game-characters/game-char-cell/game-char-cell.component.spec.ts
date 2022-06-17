import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GameCharCellComponent } from './game-char-cell.component';

describe('GameCharCellComponent', () => {
  let component: GameCharCellComponent;
  let fixture: ComponentFixture<GameCharCellComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ GameCharCellComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(GameCharCellComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
