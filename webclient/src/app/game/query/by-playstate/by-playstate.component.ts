import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {Title} from "@angular/platform-browser";
import {GamePlayStates, GameService} from "../../game.service";
import {Brand, Game} from "../../../entity";

@Component({
  selector: 'app-by-playstate',
  templateUrl: './by-playstate.component.html',
  styleUrls: ['./by-playstate.component.css']
})
export class ByPlaystateComponent implements OnInit {
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
      if (p.playState != null) {

        // this.titleService.setTitle(`Query By playState:${p['playState']}`)
        // @ts-ignore
        const filter = {'playState': GamePlayStates[p.playState].value}

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
