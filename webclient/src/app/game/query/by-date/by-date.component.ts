import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {Title} from "@angular/platform-browser";
import {Game} from "../../../entity";
import {GameService} from "../../game.service";
import {DataQuery} from "../DataQuery";

@Component({
  selector: 'app-by-date',
  templateUrl: './by-date.component.html',
  styleUrls: ['./by-date.component.css']
})
export class ByDateComponent extends DataQuery implements OnInit {

  constructor(private route: ActivatedRoute,
              private service: GameService,
              private titleService: Title
  ) {
    super()
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


        const startDate = new Date(start)
        const endDate = new Date(end)

        console.log(startDate, endDate)


        // if (startDate.getFullYear() == endDate.getFullYear()) {
        this.titleService.setTitle(`${startDate.getFullYear()}-${startDate.getMonth()} to ${endDate.getFullYear()}-${endDate.getMonth()}`)
        // } else {
        //   this.titleService.setTitle(`Year: ${startDate.getFullYear()} Month: ${startDate.getMonth()}`)
        // }

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
