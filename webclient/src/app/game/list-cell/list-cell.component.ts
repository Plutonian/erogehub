import {Component, Input, OnInit} from '@angular/core';
import {Game} from "../../entity";

@Component({
  selector: 'app-list-cell',
  templateUrl: './list-cell.component.html',
  styleUrls: ['./list-cell.component.css']
})
export class ListCellComponent implements OnInit {
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
