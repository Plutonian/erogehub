import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {Title} from "@angular/platform-browser";
import {DataQuery} from "../DataQuery";
import {Game} from "../../../entity";
import {GameService} from "../../game.service";

@Component({
  selector: 'app-by-cv',
  templateUrl: './by-cv.component.html',
  styleUrls: ['./by-cv.component.css']
})
export class ByCVComponent extends DataQuery implements OnInit {

  constructor(private route: ActivatedRoute,
              private service: GameService,
              private titleService: Title,
  ) {
    super()
  }


  ngOnInit(): void {

    this.route.params.subscribe(p => {
      // @ts-ignore
      const cv = p.cv
      // @ts-ignore
      const real = p.real


      if (cv && real) {


        this.titleService.setTitle(`${cv}`)


        if (real == 'true') {
          const filter = {'gamechar.truecv': cv}

          this.filter = JSON.stringify(filter)
        } else {
          const filter = {'gamechar.cv': cv}
          this.filter = JSON.stringify(filter)
        }

        // const filter = {'gamechar.cv': cv}
        // this.filter = JSON.stringify(filter)

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
