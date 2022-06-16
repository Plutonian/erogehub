import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {Emotions} from "../../../brand/brand.service";
import {Game} from "../../../entity";
import {GameService} from "../../game.service";
import {DataQuery} from "../DataQuery";

@Component({
  selector: 'app-by-emotion',
  templateUrl: './by-emotion.component.html',
  styleUrls: ['./by-emotion.component.css']
})
export class ByEmotionComponent extends DataQuery implements OnInit {


  constructor(
    private route: ActivatedRoute,
    private service: GameService
  ) {
    super()
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
