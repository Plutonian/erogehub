import {Component, Input, OnInit} from '@angular/core';
import {Game, PlayState} from "../../../entity";
import {GameService} from "../../game.service";

const ColorMap: Record<PlayState, string> = {
  [PlayState.PLAYED]: "rgba(0,157,255,0.56)",
  [PlayState.PLAYING]: "green",
  [PlayState.NOT_PLAY]: "red"
}

@Component({
    selector: 'app-game-playstate-change',
    templateUrl: './playstate-change.component.html',
    styleUrls: ['./playstate-change.component.css'],
    standalone: false
})
export class PlaystateChangeComponent implements OnInit {
  @Input()
  game: Game

  @Input()
  row = "row"


  playStates = [
    PlayState.PLAYED,
    PlayState.PLAYING,
    PlayState.NOT_PLAY
  ]


  fillLocation() {
    return ColorMap[this.game?._playState]
  }


  updatePlaystate() {
    const playState = this.game._playState;
    console.log(playState);

    this.gameService.changePlayState(this.game.id, playState as number)
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
