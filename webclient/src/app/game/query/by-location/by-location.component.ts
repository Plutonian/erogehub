import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {GameLocation, GameService} from "../../game.service";
import {Brand, Game} from "../../../entity";

@Component({
  selector: 'app-by-location',
  templateUrl: './by-location.component.html',
  styleUrls: ['./by-location.component.css']
})
export class ByLocationComponent implements OnInit {
  rowGameList: Game[]

  filter

  gamelist: Game[]

  constructor(
    private route: ActivatedRoute,
    private service: GameService
  ) {
  }

  ngOnInit(): void {

    this.route.params.subscribe(p => {

      // @ts-ignore
      if (p.location != null) {

        // @ts-ignore
        const filter = {'location': GameLocation[p.location].value}

        this.filter = JSON.stringify(filter)

        this.service.query(this.filter)
          .subscribe((gs: Game[]) => {

              this.gamelist = gs
              this.rowGameList = gs

              console.log("Gs", gs);

            }
          )


      }

    })
  }

  onGameDelete(game: Game) {

    console.log(game);
    this.gamelist = this.gamelist.filter(g => g.id != game.id)
    this.rowGameList = this.rowGameList.filter(g => g.id != game.id)
  }

  onBrandSelected(brand: Brand) {

    this.gamelist = this.rowGameList.filter(g => g.brand.id == brand.id)

  }

  onEmotionSelected(emotion: string) {
    this.gamelist = this.rowGameList.filter(g => g.emotion == emotion)
  }
}
