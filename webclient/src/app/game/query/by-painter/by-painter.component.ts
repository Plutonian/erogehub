import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {Title} from "@angular/platform-browser";
import {Game} from "../../../entity";
import {GameService} from "../../game.service";
import {DataQuery} from "../DataQuery";

@Component({
  selector: 'app-by-painter',
  templateUrl: './by-painter.component.html',
  styleUrls: ['./by-painter.component.css']
})
export class ByPainterComponent extends DataQuery implements OnInit {

  constructor(private route: ActivatedRoute,
              private service: GameService,
              private titleService: Title
  ) {
    super()
  }


  ngOnInit(): void {

    this.route.params.subscribe(p => {

      // @ts-ignore
      if (p.painter != null) {
        this.titleService.setTitle(`Painter: ${p['painter']}`)

        // @ts-ignore
        const filter = {'painter': p.painter}

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
