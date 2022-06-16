import {Component, OnInit} from '@angular/core';
import {Game} from "../../../entity";
import {ActivatedRoute} from "@angular/router";
import {GameService} from "../../game.service";
import {Title} from "@angular/platform-browser";
import {DataQuery} from "../DataQuery";

@Component({
  selector: 'app-by-writer',
  templateUrl: './by-writer.component.html',
  styleUrls: ['./by-writer.component.css']
})
export class ByWriterComponent extends DataQuery implements OnInit {


  constructor(private route: ActivatedRoute,
              private service: GameService,
              private titleService: Title
  ) {
    super()
  }


  ngOnInit(): void {

    this.route.params.subscribe(p => {

      // @ts-ignore
      if (p.writer != null) {
        this.titleService.setTitle(`Painter: ${p['writer']}`)

        // @ts-ignore
        const filter = {'writer': p.writer}

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
