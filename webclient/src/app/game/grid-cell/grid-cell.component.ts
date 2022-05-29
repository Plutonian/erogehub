import {Component, Input, OnInit} from '@angular/core';
import {Game} from "../../entity";

@Component({
  selector: 'app-grid-cell',
  templateUrl: './grid-cell.component.html',
  styleUrls: ['./grid-cell.component.css']
})
export class GridCellComponent implements OnInit {

  @Input()
  game: Game

  publishDate

  constructor() {

  }

  ngOnInit(): void {

    if (this.game?.publishDate != null) {
      // @ts-ignore
      this.publishDate = new Date(this.game.publishDate.year, this.game.publishDate.monthValue - 1, this.game.publishDate.dayOfMonth)
    }

  }

}
