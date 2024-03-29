import {Component, OnInit} from '@angular/core';
import {Emotions} from "./brand.service";

@Component({
  selector: 'app-brand',
  templateUrl: './brand.component.html',
  styleUrls: ['./brand.component.css']
})
export class BrandComponent implements OnInit {

  filter


  searchType = 'brand'

  searchKey: string

  state: string

  emotions = [
    "LIKE",
    "HOPE",
    "NORMAL",
    "HATE"
  ]


  onStateSelected() {

    // console.log(this.state);


    const brandState = Emotions[`${this.state}`];

    this.filter = JSON.stringify({"type": brandState.value})
  }

  ngOnInit(): void {
    // this.appService.emotions().subscribe((data: Emotion[]) => {
    this.state = "LIKE"

    this.onStateSelected()
    //   // this.states = emotions.filter(data => data.value > 0)
    // })
  }

  search() {


    const v1 = {"comp": {"$regex": `^${this.searchKey}`}}
    const v2 = {"name": {"$regex": `${this.searchKey}`}}

    let filter

    if (this.searchType == "comp") {
      filter = v1
    } else {
      filter = v2
    }

    this.filter = JSON.stringify(filter)

  }

}
