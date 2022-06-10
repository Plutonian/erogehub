import {Component, OnInit} from '@angular/core';
import {Router} from "@angular/router";
import {HttpClient} from "@angular/common/http";
import {DateGroupItem} from "./entity";
import {environment} from "../environments/environment";
import {Title} from "@angular/platform-browser";


@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  title = 'XXXXX';

  constructor
  (
    private router: Router,
    private httpClient: HttpClient,
    private titleService: Title,
  ) {
  }

  ngOnInit() {
    this.titleService.setTitle(this.title)

    this.httpClient.get(`http://${environment.APP_SERVER}/api/app/years/near`)
      .subscribe((data: DateGroupItem[]) => this.nearDates = data)

    this.httpClient.get(`http://${environment.APP_SERVER}/api/app/years/old`)
      .subscribe((data: DateGroupItem[]) => this.oldDates = data)

    this.httpClient.get(`http://${environment.APP_SERVER}/api/app/monthsOfThisYear`)
      .subscribe((data: DateGroupItem[]) => this.months = data)
  }

  nearDates: DateGroupItem[]

  oldDates: DateGroupItem[]

  months: DateGroupItem[]


  emotions = [
    "LIKE",
    "HOPE",
    // "NORMAL",
    "HATE"
  ]

  playStates = [
    "PLAYED",
    "PLAYING"
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
