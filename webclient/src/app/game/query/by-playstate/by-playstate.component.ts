import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {Title} from "@angular/platform-browser";
import {GamePlayStates, GameService} from "../../game.service";
import {Game} from "../../../entity";
import {DataQuery} from "../DataQuery";

@Component({
  selector: 'app-by-playstate',
  templateUrl: './by-playstate.component.html',
  styleUrls: ['./by-playstate.component.css']
})
export class ByPlaystateComponent extends DataQuery implements OnInit {


  constructor(private route: ActivatedRoute,
              private service: GameService,
              private titleService: Title,
  ) {
    super()
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
