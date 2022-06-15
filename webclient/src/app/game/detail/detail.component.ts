import {Component, Input, OnInit} from '@angular/core';
import {Game} from "../../entity";
import {GameService} from "../game.service";
import {ActivatedRoute} from "@angular/router";
import {Title} from "@angular/platform-browser";


@Component({
  selector: 'app-game-detail',
  templateUrl: './detail.component.html',
  styleUrls: ['./detail.component.css']
})
export class DetailComponent implements OnInit {

  // @ts-ignore
  @Input()
  game: Game = null


  constructor(private service: GameService, private route: ActivatedRoute,
              private titleService: Title,
  ) {

  }

  ngOnInit(): void {

    if (this.game == null)
      this.route.params.subscribe(p => {

        // @ts-ignore
        this.service.info(p.id)
          .subscribe((game: Game) => {

            this.game = game

            // console.log(this.route.data["breadcrumb"]);
            this.titleService.setTitle(game.name)
          })

      })

  }

}





