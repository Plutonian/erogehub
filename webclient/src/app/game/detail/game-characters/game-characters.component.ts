import {Component, Input, OnChanges, OnInit, SimpleChanges} from '@angular/core';
import {Game, GameCharacter} from "../../../entity";

@Component({
    selector: 'app-game-characters',
    templateUrl: './game-characters.component.html',
    styleUrls: ['./game-characters.component.css'],
    standalone: false
})
export class GameCharactersComponent implements OnInit, OnChanges {

  @Input()
  game: Game

  womenCharacters: GameCharacter[] = []

  menCharacters: GameCharacter[] = []

  ngOnInit(): void {


  }

  ngOnChanges(changes: SimpleChanges): void {
    if (this.game?.gamechar?.length > 0) {
      this.menCharacters = this.game.gamechar.filter(c => !c.heroine)
      this.womenCharacters = this.game.gamechar.filter(c => c.heroine)
    }
  }

}
