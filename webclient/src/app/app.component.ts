import {Component, OnInit} from '@angular/core';
import {Router} from "@angular/router";
import {HttpClient} from "@angular/common/http";
import {APP_SERVER} from "./app.module";
import {DateGroupItem, DateRange} from "./entity";

export interface DateCommand {
  title: string
  range: DateRange
}

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  title = 'Webclient';

  constructor(private router: Router, private httpClient: HttpClient) {
  }

  ngOnInit() {

    this.httpClient.get(`http://${APP_SERVER}/api/app/nearYears`)
      .subscribe((data: DateGroupItem[]) => this.dates = data)

    this.httpClient.get(`http://${APP_SERVER}/api/app/monthsOfThisYear`)
      .subscribe((data: DateGroupItem[]) => this.months = data)
  }

  dates: DateGroupItem[]

  months: DateGroupItem[]

  states = [
    "PLAYED",
    "PLAYING",
    "HOPE",
    "SAME"
  ]

  locations = [
    "LOCAL"
    // GameLocation.REMOTE
  ]

  stars = [5, 4, 3, 2, 1]

  jump(dateCommand: DateGroupItem) {

    // Date.parse(`${dateCommand.start} 00:00:00`)

    const start = Date.parse(`${dateCommand.range.start} 00:00:00`)
    const end = Date.parse(`${dateCommand.range.end} 23:59:59`)

    console.log(start, end)
    // console.log(start.getTime(), end.getTime())

    const filter = {"publishDate": {"$gte": {"$date": start}, "$lte": {"$date": end}}}
    this.router.navigateByUrl(`/game/query/${JSON.stringify(filter)}`)
  }

}
