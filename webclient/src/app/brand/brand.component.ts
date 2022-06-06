import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {NgForm} from "@angular/forms";
import {Emotion} from "../entity";
import {AppService} from "../app.service";

@Component({
  selector: 'app-brand',
  templateUrl: './brand.component.html',
  styleUrls: ['./brand.component.css']
})
export class BrandComponent implements OnInit {

  filter

  state: string

  states: Emotion[]

  constructor(private route: ActivatedRoute, private appService: AppService) {
  }

  onStateSelected() {

    this.filter = JSON.stringify({"type": parseInt(this.state)})
  }

  ngOnInit(): void {
    this.appService.emotions().subscribe((data: Emotion[]) => {
      this.states = data
      // this.states = emotions.filter(data => data.value > 0)
    })
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
