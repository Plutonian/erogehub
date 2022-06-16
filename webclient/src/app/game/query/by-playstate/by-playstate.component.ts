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

  playState

  constructor(private route: ActivatedRoute,
              private service: GameService,
              private titleService: Title,
  ) {
    super()
  }

  ngOnInit(): void {

    this.route.params.subscribe(p => {

      // @ts-ignore
      this.playState = p.playState

      if (this.playState != null) {

        this.titleService.setTitle(`${this.playState}`)
        // @ts-ignore
        const filter = {'playState': GamePlayStates[this.playState].value}

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
