import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Game, Emotions} from "../../../entity";

@Component({
    selector: 'app-game-listview',
    templateUrl: 'game-list-view.component.html',
    styleUrls: ['game-list-view.component.css'],
    standalone: false
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
