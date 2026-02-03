import {Component, Input, OnInit} from '@angular/core';
import {Game, Emotion} from "../../../entity";
import {GameService} from "../../game.service";

@Component({
    selector: 'app-emotion-change',
    templateUrl: './emotion-change.component.html',
    styleUrls: ['./emotion-change.component.css'],
    standalone: false
})
export class EmotionChangeComponent implements OnInit {
  @Input()
  game: Game

  @Input()
  row = "row"

  emotions = Object.values(Emotion).filter(v => typeof v === 'number')


  updateEmotion() {
    const emotionValue = Number(this.game.emotion);
    console.log(emotionValue);

    this.gameService.changeEmotion(this.game.id, emotionValue)
      .subscribe((data: string) =>
        console.log(data)
      )
  }

  constructor(private gameService: GameService) {
  }

  ngOnInit(): void {
  }
}
