import {Component, OnInit} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {APP_SERVER} from "../app.module";
import {CVGroup} from "../entity";
import {Router} from "@angular/router";

@Component({
  selector: 'app-cv',
  templateUrl: './cv.component.html',
  styleUrls: ['./cv.component.css']
})
export class CvComponent implements OnInit {

  cvGroup: CVGroup[]

  jumpCV(cv) {

    const filter = {"gamechar.truecv": cv}
    this.router.navigateByUrl(`/game/query/${JSON.stringify(filter)}`)
  }

  constructor(private httpClient: HttpClient, private router: Router) {
  }

  ngOnInit(): void {
    this.httpClient.get(`http://${APP_SERVER}/api/cv`)
      .subscribe((data: CVGroup[]) => this.cvGroup = data)
  }

}
