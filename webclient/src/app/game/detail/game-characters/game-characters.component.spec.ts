import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GameCharactersComponent } from './game-characters.component';

describe('GameCharactersComponent', () => {
  let component: GameCharactersComponent;
  let fixture: ComponentFixture<GameCharactersComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ GameCharactersComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(GameCharactersComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
