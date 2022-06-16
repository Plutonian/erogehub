import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {Brand, Game} from "../../../entity";
import {GameService} from "../../game.service";
import {Title} from "@angular/platform-browser";

@Component({
  selector: 'app-by-tag',
  templateUrl: './by-tag.component.html',
  styleUrls: ['./by-tag.component.css']
})
export class ByTagComponent implements OnInit {

  rowGameList: Game[]

  filter

  gamelist: Game[]


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

  constructor(private route: ActivatedRoute,
              private service: GameService,
              private titleService: Title,
  ) {
  }

  ngOnInit(): void {

    this.route.params.subscribe(p => {

      // @ts-ignore
      if (p.tag != null) {
        // @ts-ignore
        const filter = {'tag': p.tag}

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
