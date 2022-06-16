import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {GameLocation, GameService} from "../../game.service";
import {Game} from "../../../entity";
import {DataQuery} from "../DataQuery";
import {Title} from "@angular/platform-browser";

@Component({
  selector: 'app-by-location',
  templateUrl: './by-location.component.html',
  styleUrls: ['./by-location.component.css']
})
export class ByLocationComponent extends DataQuery implements OnInit {

  location


  constructor(
    private route: ActivatedRoute,
    private service: GameService,
    private titleService: Title,
  ) {
    super()
  }

  ngOnInit(): void {

    this.route.params.subscribe(p => {

      // @ts-ignore
      this.location = p.location
      this.titleService.setTitle(this.location)

      if (this.location != null) {

        const filter = {'location': GameLocation[this.location].value}

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
