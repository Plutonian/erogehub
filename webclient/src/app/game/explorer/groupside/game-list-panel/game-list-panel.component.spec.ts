import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GameListPanelComponent } from './game-list-panel.component';

describe('GameListPanelComponent', () => {
  let component: GameListPanelComponent;
  let fixture: ComponentFixture<GameListPanelComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ GameListPanelComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(GameListPanelComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
