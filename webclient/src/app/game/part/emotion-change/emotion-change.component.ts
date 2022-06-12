import {Component, Input, OnInit} from '@angular/core';
import {Game} from "../../../entity";
import {GameService} from "../../game.service";
import {Emotions} from "../../../brand/brand.service";

@Component({
  selector: 'app-emotion-change',
  templateUrl: './emotion-change.component.html',
  styleUrls: ['./emotion-change.component.css']
})
export class EmotionChangeComponent implements OnInit {
  @Input()
  game: Game

  @Input()
  row = "row"

  emotions = [
    "LIKE",
    "HOPE",
    "NORMAL",
    "HATE",
  ]


  changeEmotion(emotion: string) {
    this.game.emotion = emotion
    this.updateEmotion()
  }

  updateEmotion() {
    const emotion = Emotions[`${this.game.emotion}`];
    console.log(emotion);

    this.gameService.changeEmotion(this.game.id, emotion.value)
      .subscribe((data: string) =>
        console.log(data)
      )
  }

  constructor(private gameService: GameService) {
  }

  ngOnInit(): void {
  }
}
