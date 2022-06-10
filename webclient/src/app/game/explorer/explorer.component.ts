import {Component, Input, OnChanges, SimpleChanges} from '@angular/core';
import {Brand, Game} from "../../entity";
import {GameService} from "../game.service";


@Component({
  selector: 'app-explorer',
  templateUrl: './explorer.component.html',
  styleUrls: ['explorer.component.css']
})
export class ExplorerComponent implements OnChanges {

  rowGameList: Game[]

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

  onBrandSelected(brand: Brand) {

    this.gamelist = this.rowGameList.filter(g => g.brand.id == brand.id)

  }

  onEmotionSelected(emotion: string) {
    this.gamelist = this.rowGameList.filter(g => g.emotion == emotion)
  }

  clearFilter() {
    this.gamelist = this.rowGameList
  }


  ngOnChanges(changes: SimpleChanges): void {

    // @ts-ignore
    const filter = changes.filter.currentValue

    console.log(`ExplorerComponent :${filter}`);

    if (filter != null) {
      this.service.query(filter)
        .subscribe((gs: Game[]) => {

            this.gamelist = gs
            this.rowGameList = gs

            console.log("Gs", gs);

          }
        )


    }

  }

  gameDelete(game: Game) {
    console.log(game);
    this.gamelist = this.gamelist.filter(g => g.id != game.id)
    this.rowGameList = this.rowGameList.filter(g => g.id != game.id)
  }

}
