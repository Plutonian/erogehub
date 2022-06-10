import {Component, Input, OnInit} from '@angular/core';
import {Game} from "../../../entity";
import {GamePlayStates, GameService} from "../../game.service";

@Component({
  selector: 'app-game-playstate-change',
  templateUrl: './playstate-change.component.html',
  styleUrls: ['./playstate-change.component.css']
})
export class PlaystateChangeComponent implements OnInit {
  @Input()
  game: Game

  @Input()
  row = "row"

  edit: Boolean = false


  playStates = [
    "PLAYED",
    "PLAYING",
    "NOT_PLAY"
  ]

  change() {
    this.edit = !this.edit
  }

  onStateSelected() {
    this.change()

    this.updateState()
  }

  updateState() {
    const playState = GamePlayStates[`${this.game.playState}`];

    console.log(playState);

    this.gameService.changePlayState(this.game.id, playState.value)
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
