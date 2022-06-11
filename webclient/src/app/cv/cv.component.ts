import {Component, OnInit} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {CVGroup} from "../entity";
import {environment} from "../../environments/environment";
import {Title} from "@angular/platform-browser";

@Component({
  selector: 'app-cv',
  templateUrl: './cv.component.html',
  styleUrls: ['./cv.component.css']
})
export class CvComponent implements OnInit {

  cvGroup: CVGroup[]

  constructor(private httpClient: HttpClient, private titleService: Title) {
  }

  ngOnInit(): void {
    this.titleService.setTitle("CV")


    this.httpClient.get(`http://${environment.APP_SERVER}/api/cv`)
      .subscribe((data: CVGroup[]) => this.cvGroup = data)
  }

}
