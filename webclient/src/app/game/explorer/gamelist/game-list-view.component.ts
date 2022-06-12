import {Component, Input, OnInit} from '@angular/core';
import {Game} from "../../../entity";
import {Emotions} from "../../../brand/brand.service";

@Component({
  selector: 'app-game-listview',
  templateUrl: 'game-list-view.component.html',
  styleUrls: ['game-list-view.component.css']
})
export class GameListViewComponent implements OnInit {

  @Input()
  rowGameList: Game[]

  size? = "Grid"

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
    console.log(game);
    this.gamelist = this.gamelist.filter(g => g.id != game.id)
    this.rowGameList = this.rowGameList.filter(g => g.id != game.id)
  }

  ngOnInit(): void {
    this.rowGameList = this.gamelist
  }

  // ngOnChanges(changes: SimpleChanges): void {
  // }

}
