import {Component, OnInit} from '@angular/core';
import {Router} from "@angular/router";
import {HttpClient} from "@angular/common/http";
import {APP_SERVER} from "./app.module";

interface DateCommand {
  name: String
  start: String
  end: String
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

    this.httpClient.get(`http://${APP_SERVER}/api/app`)
      .subscribe((data: DateCommand[]) => this.dates = data)

  }

  dates: DateCommand[]

  states = [
    "PLAYED",
    "PLAYING",
    "HOPE"
  ]

  locations = [
    "LOCAL"
    // GameLocation.REMOTE
  ]

  stars = [5, 4, 3]

  jump(dateCommand: DateCommand) {

    // Date.parse(`${dateCommand.start} 00:00:00`)

    const start = Date.parse(`${dateCommand.start} 00:00:00`)
    const end = Date.parse(`${dateCommand.end} 23:59:59`)

    console.log(start, end)
    // console.log(start.getTime(), end.getTime())

    const filter = {"publishDate": {"$gte": {"$date": start}, "$lte": {"$date": end}}}
    this.router.navigateByUrl(`/game/query/${JSON.stringify(filter)}`)
  }


  jumpLocation(location) {

    const filter = {"location": location}
    this.router.navigateByUrl(`/game/query/${JSON.stringify(filter)}`)

  }


}
