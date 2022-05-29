import {Component, OnInit} from '@angular/core';
import {Game} from "../../entity";
import {GameService} from "../game.service";
import {ActivatedRoute} from "@angular/router";


@Component({
  selector: 'app-detail',
  templateUrl: './detail.component.html',
  styleUrls: ['./detail.component.css']
})
export class DetailComponent implements OnInit {

  // @ts-ignore
  g: Game = null

  // {
  //   "id": 0,
  //   "name": "",
  //   "publishDate": {
  //     "year": 2021,
  //     "monthValue": 9,
  //     "dayOfMonth": 24
  //   },
  //   "painter": [],
  //   "tag": [],
  //   "story": "",
  //   "intro": "",
  //   "gameCharacters": [{
  //     "name": "",
  //     "cv": "",
  //     "intro": "",
  //     "trueCV": "",
  //     "index": 1
  //   }],
  //   "gameImgs": [{
  //     "index": 1
  //   }],
  //   "state": "PLAYED",
  //   "location": "LOCAL",
  //   "star": 4,
  //   "brand": {
  //     "id": 27875,
  //     "name": "",
  //     "website": "",
  //     "comp": "",
  //     "state": "LIKE"
  //   },
  //   "titles": {"mainTitle": "", "subTitle": ""}
  // }


  constructor(private service: GameService, private route: ActivatedRoute) {

  }

  ngOnInit(): void {

    this.route.params.subscribe(p => {

      // @ts-ignore
      this.service.info(p.id)
        .subscribe((game: Game) => {
          // if (game != null && game.publishDate != null) {
          //   // @ts-ignore
          //   game.publishDate = myDate2JSDate(game.publishDate)
          // }

          this.g = game
        })

    })

  }

}




