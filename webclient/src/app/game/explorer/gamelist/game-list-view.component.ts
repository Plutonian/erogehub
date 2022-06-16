import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Game} from "../../../entity";
import {Emotions} from "../../../brand/brand.service";

@Component({
  selector: 'app-game-listview',
  templateUrl: 'game-list-view.component.html',
  styleUrls: ['game-list-view.component.css']
})
export class GameListViewComponent implements OnInit {

  @Output()
  onGameDelete = new EventEmitter<Game>()

  @Input()
  rowGameList: Game[]

  @Input()
  size = "Grid"

  @Input()
  gamelist: Game[]


  sort() {
    this.gamelist.sort((a, b) => a.name.localeCompare(b.name))
  }

  sortEmotion() {
    this.gamelist.sort((a, b) => Emotions[b.emotion].value - Emotions[a.emotion].value)
  }


  clearFilter() {
    this.gamelist = this.rowGameList
  }

  gameDelete(game: Game) {
    this.onGameDelete.emit(game)
  }

  ngOnInit(): void {
    this.rowGameList = this.gamelist
  }

}
