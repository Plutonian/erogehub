import {Component, Input, OnChanges, OnInit, SimpleChanges} from '@angular/core';
import {Game, GameCharacter} from "../../../entity";

@Component({
  selector: 'app-game-characters',
  templateUrl: './game-characters.component.html',
  styleUrls: ['./game-characters.component.css']
})
export class GameCharactersComponent implements OnInit, OnChanges {

  @Input()
  game: Game

  womenCharacters: GameCharacter[] = []

  menCharacters: GameCharacter[] = []

  ngOnInit(): void {


  }

  ngOnChanges(changes: SimpleChanges): void {
    if (this.game?.gameCharacters?.length > 0) {
      this.menCharacters = this.game.gameCharacters.filter(c => !c.heroine)
      this.womenCharacters = this.game.gameCharacters.filter(c => c.heroine)
    }
  }

}
