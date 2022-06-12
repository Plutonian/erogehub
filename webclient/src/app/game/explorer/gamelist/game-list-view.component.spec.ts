import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GameListViewComponent } from './game-list-view.component';

describe('GamelistComponent', () => {
  let component: GameListViewComponent;
  let fixture: ComponentFixture<GameListViewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ GameListViewComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(GameListViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
