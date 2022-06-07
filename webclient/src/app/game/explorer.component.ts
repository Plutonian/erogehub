import {Component, Input, OnChanges, SimpleChanges} from '@angular/core';
import {Game} from "../entity";
import {GameService} from "./game.service";


@Component({
  selector: 'app-explorer',
  templateUrl: './explorer.component.html',
  styleUrls: ['./explorer.component.css']
})
export class ExplorerComponent implements OnChanges {

  @Input()
  filter

  size? = "Grid"


  gamelist: Game[]

  constructor(private service: GameService) {
  }

  sort() {
    this.gamelist.sort((a, b) => a.name.localeCompare(b.name))
  }

  sortState() {
    this.gamelist.sort((a, b) => b.state.localeCompare(a.state))
  }


  ngOnChanges(changes: SimpleChanges): void {

    // @ts-ignore
    const filter = changes.filter.currentValue

    console.log(`ExplorerComponent :${filter}`);

    if (filter != null) {
      this.service.query(filter)
        .subscribe((gs: Game[]) => {

            // if (gs != null) {
            //   // @ts-ignore
            //   gs.filter(g => g.publishDate != null).forEach(g => g.publishDate = myDate2JSDate(g.publishDate))
            // }

            this.gamelist = gs

            console.log("Gs", gs);

            if (gs != null && gs.length > 0) {

            }
          }
        )


    }

  }

  gameDelete(game: Game) {
    console.log(game);
    this.gamelist = this.gamelist.filter(g => g.id != game.id)
  }

}
