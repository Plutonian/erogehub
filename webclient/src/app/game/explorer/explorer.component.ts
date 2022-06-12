import {Component, Input, OnChanges, SimpleChanges} from '@angular/core';
import {Brand, Game} from "../../entity";
import {GameService} from "../game.service";
import {Emotions} from "../../brand/brand.service";


@Component({
  selector: 'app-explorer',
  templateUrl: './explorer.component.html',
  styleUrls: ['explorer.component.css']
})
export class ExplorerComponent implements OnChanges {

  rowGameList: Game[]

  @Input()
  filter

  gamelist: Game[]

  constructor(private service: GameService) {
  }


  onBrandSelected(brand: Brand) {

    this.gamelist = this.rowGameList.filter(g => g.brand.id == brand.id)

  }

  onEmotionSelected(emotion: string) {
    this.gamelist = this.rowGameList.filter(g => g.emotion == emotion)
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



}
