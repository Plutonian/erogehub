import {Brand, Emotion, Game} from "../../entity";
import {Component, Input} from "@angular/core";

@Component({
  // selector: '',
  standalone: true,
  template: '',
  // styleUrl: './tag-search.component.css'
})
export abstract class DataQuery {

  @Input()
  gamelist: Game[]

  onGameDelete(game: Game) {

    console.log(game);
    this.gamelist = this.gamelist.filter(g => g.id != game.id)
  }
  }
