import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, NavigationEnd, Router} from "@angular/router";
import {HttpClient} from "@angular/common/http";
import {DateGroupItem} from "./entity";
import {environment} from "../environments/environment";
import {Title} from "@angular/platform-browser";
import {filter} from "rxjs";


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
    private activatedRoute: ActivatedRoute,
    private titleService: Title,
  ) {
  }


  ngOnInit() {
    // this.titleService.setTitle(this.title)

    this.httpClient.get(`http://${environment.APP_SERVER}/api/app/years/near`)
      .subscribe((data: DateGroupItem[]) => this.nearDates = data)

    this.httpClient.get(`http://${environment.APP_SERVER}/api/app/years/old`)
      .subscribe((data: DateGroupItem[]) => this.oldDates = data)

    this.httpClient.get(`http://${environment.APP_SERVER}/api/app/monthsOfThisYear`)
      .subscribe((data: DateGroupItem[]) => this.months = data)


    this.httpClient.get(`http://${environment.APP_SERVER}/api/app/years/this`)
      .subscribe((data: DateGroupItem) => this.thisYear = data)

    this.httpClient.get(`http://${environment.APP_SERVER}/api/app/years/this/month/this`)
      .subscribe((data: DateGroupItem) => this.thisMonth = data)


    // this.router.events.pipe(
    //   filter(event => event instanceof NavigationEnd),
    // )
    //   .subscribe(() => {
    //
    //     const rt = this.getChild(this.activatedRoute)
    //
    //     rt.data.subscribe(data => {
    //       console.log(data);
    //       // data.breadcrumb= this.titleService.getTitle()
    //       this.titleService.setTitle(data.breadcrumb)
    //
    //     })
    //   })
  }

  getChild(activatedRoute: ActivatedRoute) {
    if (activatedRoute.firstChild) {
      return this.getChild(activatedRoute.firstChild);
    } else {
      return activatedRoute;
    }

  }

  nearDates: DateGroupItem[]

  oldDates: DateGroupItem[]

  months: DateGroupItem[]

  thisYear: DateGroupItem

  thisMonth: DateGroupItem


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
    // "REMOTE"
  ]

  stars = [5, 4, 3, 2, 1]

  jump(dateCommand: DateGroupItem) {

    this.router.navigateByUrl(`/game/date/${dateCommand.range.start}/${dateCommand.range.end}`)
  }

}
