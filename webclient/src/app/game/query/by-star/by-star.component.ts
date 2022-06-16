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

  star: number

  constructor(private route: ActivatedRoute,
              private service: GameService,
              private titleService: Title,
  ) {
    super()
  }


  ngOnInit(): void {

    this.route.params.subscribe(p => {


      if (p['star'] != null) {

        this.star = parseInt(p['star'])

        this.titleService.setTitle(`Star: ${this.star}`)

        const filter = {'star': this.star}

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
