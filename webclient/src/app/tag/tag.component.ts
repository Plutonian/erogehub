import {Component, OnInit} from '@angular/core';
import {APP_SERVER} from "../app.module";
import {TagGroup} from "../entity";
import {HttpClient} from "@angular/common/http";
import {Router} from "@angular/router";

@Component({
  selector: 'app-tag',
  templateUrl: './tag.component.html',
  styleUrls: ['./tag.component.css']
})
export class TagComponent implements OnInit {

  tagGroup: TagGroup[]

  constructor(private httpClient: HttpClient, private router: Router) {
  }

  ngOnInit(): void {
    this.httpClient.get(`http://${APP_SERVER}/api/tags`)
      .subscribe((data: TagGroup[]) => this.tagGroup = data)
  }

}
