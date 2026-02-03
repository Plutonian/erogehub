import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Emotion, Game} from "../../../../entity";
import {GameService} from "../../../game.service";

const ColorMap = {
    "PLAYED": "rgba(0,157,255,0.56)",
    "PLAYING": "green",
    "NOT_PLAY": "red"
}

@Component({
    selector: 'app-grid-cell',
    templateUrl: './grid-cell.component.html',
    styleUrls: ['./grid-cell.component.css'],
    standalone: false
})
export class GridCellComponent implements OnInit {

  private readonly today = new Date()

  @Input()
  game: Game

  @Input()
  large = true

  @Output()
  remove = new EventEmitter<Game>();
    showGameDetail=false;

  constructor(private gameService: GameService) {

  }

  ngOnInit(): void {


  }

  delete() {
    this.gameService.delete(this.game.id)
      .subscribe((data) => {
        console.log(data)
        this.remove.emit(this.game)
      })

  }


  published() {

      let date= this.game._publishDate

    return this.today.getTime() - date.getTime() > 0
  }




    fillLocation() {
        return ColorMap[this.game?.playState]
    }

    protected readonly Emotion = Emotion;
}
