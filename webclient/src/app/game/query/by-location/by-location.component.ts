import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {GameLocation, GameService} from "../../game.service";
import {Game} from "../../../entity";
import {DataQuery} from "../DataQuery";

@Component({
  selector: 'app-by-location',
  templateUrl: './by-location.component.html',
  styleUrls: ['./by-location.component.css']
})
export class ByLocationComponent extends DataQuery implements OnInit {


  constructor(
    private route: ActivatedRoute,
    private service: GameService
  ) {
    super()
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

}
