import {Component, Input, OnInit} from '@angular/core';
import {Game} from "../../../entity";
import {GamePlayStates, GameService} from "../../game.service";

const ColorMap = {
  "PLAYED": "rgba(0,157,255,0.56)",
  "PLAYING": "green",
  "NOT_PLAY": "red"
}

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


  playStates = [
    "PLAYED",
    "PLAYING",
    "NOT_PLAY"
  ]


  fillLocation() {
    return ColorMap[this.game?.playState]
  }


  updatePlaystate() {
    const playState = GamePlayStates[`${this.game.playState}`];

    console.log(playState);

    this.gameService.changePlayState(this.game.id, playState.value)
      .subscribe((data: string) =>
        console.log(data)
      )
  }

  // changePlaystate(playstate: string) {
  //   this.game.playState = playstate
  //   this.updatePlaystate()
  //   // this.change()
  // }

  constructor(private gameService: GameService) {
  }

  ngOnInit(): void {
  }


}
