import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {Title} from "@angular/platform-browser";
import {Game} from "../../../entity";
import {GameService} from "../../game.service";
import {DataQuery} from "../DataQuery";

@Component({
  selector: 'app-by-star',
  templateUrl: './by-star.component.html',
  styleUrls: ['./by-star.component.css']
})
export class ByStarComponent extends DataQuery implements OnInit {


  constructor(private route: ActivatedRoute,
              private service: GameService,
              private titleService: Title,
  ) {
    super()
  }


  ngOnInit(): void {

    this.route.params.subscribe(p => {

      // @ts-ignore
      if (p.star != null) {

        this.titleService.setTitle(`Star: ${p['star']}`)

        // @ts-ignore
        const filter = {'star': parseInt(p.star)}

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
