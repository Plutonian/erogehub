import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {Emotions} from "../../../brand/brand.service";
import {Brand, Game} from "../../../entity";
import {GameService} from "../../game.service";

@Component({
  selector: 'app-by-emotion',
  templateUrl: './by-emotion.component.html',
  styleUrls: ['./by-emotion.component.css']
})
export class ByEmotionComponent implements OnInit {
  rowGameList: Game[]

  filter

  gamelist: Game[]

  constructor(
    private route: ActivatedRoute,
    private service: GameService
  ) {
  }

  onGameDelete(game: Game) {

    console.log(game);
    this.gamelist = this.gamelist.filter(g => g.id != game.id)
    this.rowGameList = this.rowGameList.filter(g => g.id != game.id)
  }

  onBrandSelected(brand: Brand) {

    this.gamelist = this.rowGameList.filter(g => g.brand.id == brand.id)

  }

  ngOnInit(): void {

    this.route.params.subscribe(p => {

      // @ts-ignore
      if (p.emotion != null) {


        // @ts-ignore
        const filter = {'emotion': Emotions[p.emotion].value}

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

}
