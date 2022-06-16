import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {Title} from "@angular/platform-browser";
import {Brand, Game} from "../../../entity";
import {GameService} from "../../game.service";

@Component({
  selector: 'app-by-date',
  templateUrl: './by-date.component.html',
  styleUrls: ['./by-date.component.css']
})
export class ByDateComponent implements OnInit {

  rowGameList: Game[]

  filter

  gamelist: Game[]


  constructor(private route: ActivatedRoute,
              private service: GameService,
              private titleService: Title
  ) {
  }

  ngOnInit(): void {

    this.route.params.subscribe(p => {
      // @ts-ignore
      const startStr = p.start
      // @ts-ignore
      const endStr = p.end

      if (startStr && endStr) {


        const start = Date.parse(`${startStr} 00:00:00`)
        const end = Date.parse(`${endStr} 23:59:59`)

        console.log(start, end)
        // console.log(start.getTime(), end.getTime())

        const filter = {"publishDate": {"$gte": {"$date": start}, "$lte": {"$date": end}}}

        // @ts-ignore
        console.log(p.start, p.end)
        this.titleService.setTitle(`From: ${startStr} to ${endStr}`)

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

  onGameDelete(game: Game) {

    console.log(game);
    this.gamelist = this.gamelist.filter(g => g.id != game.id)
    this.rowGameList = this.rowGameList.filter(g => g.id != game.id)
  }

  onBrandSelected(brand: Brand) {

    this.gamelist = this.rowGameList.filter(g => g.brand.id == brand.id)

  }

  onEmotionSelected(emotion: string) {
    this.gamelist = this.rowGameList.filter(g => g.emotion == emotion)
  }

}
