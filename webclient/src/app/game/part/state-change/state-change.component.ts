import {Component, Input, OnInit} from '@angular/core';
import {Game} from "../../../entity";
import {GameService, GameStates} from "../../game.service";

@Component({
  selector: 'app-state-change',
  templateUrl: './state-change.component.html',
  styleUrls: ['./state-change.component.css']
})
export class StateChangeComponent implements OnInit {

  @Input()
  game: Game

  edit: Boolean = false


  states = [
    "PLAYED",
    "PLAYING",
    "HOPE",
    "UNCHECKED",
    "SAME",
    "PACKAGE",
    "BLOCK",
  ]

  change() {
    this.edit = !this.edit
  }

  onStateSelected() {
    this.change()

    const gameState = GameStates[`${this.game.state}`];
    console.log(gameState);
    this.gameService.changeState(this.game.id, gameState.value)
      .subscribe((data: String) =>
        console.log(data)
      )
  }

  constructor(private gameService: GameService) {
  }

  ngOnInit(): void {
    // this.selectState = this.game.state
  }


}
