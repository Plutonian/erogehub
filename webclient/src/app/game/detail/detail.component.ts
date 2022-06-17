import {Component, Input, OnInit} from '@angular/core';
import {Game} from "../../entity";
import {GameService} from "../game.service";
import {ActivatedRoute} from "@angular/router";
import {Title} from "@angular/platform-browser";
import {NzMessageService} from "ng-zorro-antd/message";


@Component({
  selector: 'app-game-detail',
  templateUrl: './detail.component.html',
  styleUrls: ['./detail.component.css']
})
export class DetailComponent implements OnInit {

  // @ts-ignore
  @Input()
  game: Game = null


  modelVisible: boolean

  showModal(): void {
    this.modelVisible = true;
  }

  handleCancel() {
    this.modelVisible = false;
  }

  updateTitle() {
    this.service.setTitle(this.game.id, this.game.titles.mainTitle, this.game.titles.subTitle)
      .subscribe((data: string) => {
        this.message.create("success", data)
      })
  }

  splitTitle() {
    const spIndex = this.game.name.indexOf(" ")

    if (spIndex != -1) {
      this.game.titles.mainTitle = this.game.name.substring(0, spIndex)
      this.game.titles.subTitle = this.game.name.substring(spIndex + 1, this.game.name.length)
    }

  }


  constructor(private service: GameService, private route: ActivatedRoute,
              private titleService: Title,
              private message: NzMessageService
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





