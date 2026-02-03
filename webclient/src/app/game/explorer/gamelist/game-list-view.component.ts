import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Game, Emotions} from "../../../entity";

@Component({
    selector: 'app-game-listview',
    templateUrl: 'game-list-view.component.html',
    styleUrls: ['game-list-view.component.css'],
    standalone: false
})
export class GameListViewComponent  {

  @Output()
  onGameDelete = new EventEmitter<Game>()

  @Input()
  size = "Grid"

  @Input()
  gamelist: Game[]

  gameDelete(game: Game) {
    this.onGameDelete.emit(game)
  }

}
