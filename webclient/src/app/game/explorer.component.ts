import {Component, Input, OnChanges, SimpleChanges} from '@angular/core';
import {BrandGroupItem, CVGroupItem, DateGroupItem, Game, TagGroupItem} from "../entity";
import {GameService} from "./game.service";
import {Arrays} from "../../main";


@Component({
  selector: 'app-explorer',
  templateUrl: './explorer.component.html',
  styleUrls: ['./explorer.component.css']
})
export class ExplorerComponent implements OnChanges {

  @Input()
  filter

  brandGroup: BrandGroupItem[]

  dateGroup: DateGroupItem[]

  cvGroup: CVGroupItem[]

  tagGroup: TagGroupItem[]

  gamelist: Game[]

  constructor(private service: GameService) {
  }


  ngOnChanges(changes: SimpleChanges): void {

    // @ts-ignore
    const filter = changes.filter.currentValue

    console.log(`ExplorerComponent :${filter}`);

    if (filter != null) {
      this.service.query(filter)
        .subscribe((gs: Game[]) => {
            this.gamelist = gs

            console.log("Gs", gs);

            if (gs != null && gs.length > 0) {
              this.service.groupByDate(filter)
                .subscribe((data: DateGroupItem[]) => this.dateGroup = data)

              this.service.groupByBrand(filter)
                .subscribe((data: BrandGroupItem[]) => this.brandGroup = data)

              this.service.groupByCV(filter)
                .subscribe((data: CVGroupItem[]) => this.cvGroup = data)

              this.service.groupByTag(filter)
                .subscribe((data: TagGroupItem[]) => this.tagGroup = data)
            }
          }
        )


    }

  }

}
