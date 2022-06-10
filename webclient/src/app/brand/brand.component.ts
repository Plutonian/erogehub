import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {NgForm} from "@angular/forms";
import {AppService} from "../app.service";
import {Emotions} from "./brand.service";

@Component({
  selector: 'app-brand',
  templateUrl: './brand.component.html',
  styleUrls: ['./brand.component.css']
})
export class BrandComponent implements OnInit {

  filter

  state: string

  emotions = [
    "LIKE",
    "HOPE",
    "NORMAL",
    "HATE"
  ]

  constructor(private route: ActivatedRoute, private appService: AppService) {
  }

  onStateSelected() {


    const brandState = Emotions[`${this.state}`];

    this.filter = JSON.stringify({"type": brandState.value})
  }

  ngOnInit(): void {
    // this.appService.emotions().subscribe((data: Emotion[]) => {
    //   this.states = data
    //   // this.states = emotions.filter(data => data.value > 0)
    // })
  }

  onSubmit(form: NgForm) {
    const {searchKey, searchType} = form.value;

    console.log(searchKey, searchType)

    const v1 = {"comp": {"$regex": `^${searchKey}`}}
    const v2 = {"name": {"$regex": `${searchKey}`}}

    let filter

    if (searchType == "0") {
      filter = v1
    } else {
      filter = v2
    }

    this.filter = JSON.stringify(filter)

  }

}
