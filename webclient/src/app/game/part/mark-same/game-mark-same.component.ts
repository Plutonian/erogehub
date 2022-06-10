import {Component, Input, OnInit} from '@angular/core';
import {Game} from "../../../entity";
import {GameService} from "../../game.service";

@Component({
  selector: 'app-game-marksame',
  templateUrl: 'game-mark-same.component.html',
  styleUrls: ['game-mark-same.component.css']
})
export class GameMarkSameComponent implements OnInit {

  @Input()
  game: Game


  onStateSelected() {
    this.updateState()
  }

  updateState() {
    this.gameService.markSame(this.game.id, this.game.isSame)
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
