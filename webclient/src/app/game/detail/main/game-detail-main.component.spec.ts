import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GameDetailMainComponent } from './game-detail-main.component';

describe('MainComponent', () => {
  let component: GameDetailMainComponent;
  let fixture: ComponentFixture<GameDetailMainComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ GameDetailMainComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(GameDetailMainComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
