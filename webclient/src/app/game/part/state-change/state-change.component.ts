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

  @Input()
  row = "row"

  edit: Boolean = false


  states = [
    "PLAYED",
    "PLAYING",
    "HOPE",
    "UNCHECKED",
    "SAME",
    "BLOCK",
  ]

  change() {
    this.edit = !this.edit
  }

  like() {
    this.game.state = "HOPE"
    this.updateState()
  }


  normal() {
    this.game.state = "UNCHECKED"
    this.updateState()
  }


  hate() {
    this.game.state = "BLOCK"
    this.updateState()
  }

  onStateSelected() {
    this.change()

    this.updateState()
  }

  updateState() {
    const gameState = GameStates[`${this.game.state}`];
    console.log(gameState);

    this.gameService.changeState(this.game.id, gameState.value)
      .subscribe((data: string) =>
        console.log(data)
      )
  }

  constructor(private gameService: GameService) {
  }

  ngOnInit(): void {
    // this.selectState = this.game.state
  }


}
