import {Component, Input, OnInit} from '@angular/core';
import {Game} from "../../../entity";
import {GameService} from "../../game.service";
import {ActivatedRoute} from "@angular/router";

@Component({
  selector: 'app-game-simple',
  templateUrl: 'game-simple.component.html',
  styleUrls: ['game-simple.component.css']
})
export class GameSimpleComponent implements OnInit {
  // @ts-ignore
  @Input()
  game: Game = null


  constructor(private service: GameService, private route: ActivatedRoute) {

  }

  ngOnInit(): void {

    if (this.game == null)
      this.route.params.subscribe(p => {

        // @ts-ignore
        this.service.info(p.id)
          .subscribe((game: Game) => {
            this.game = game
          })

      })

  }

}
