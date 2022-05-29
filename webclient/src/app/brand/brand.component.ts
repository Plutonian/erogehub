import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {NgForm} from "@angular/forms";
import {BrandStates} from "./brand.service";
import {Brand} from "../entity";

@Component({
  selector: 'app-brand',
  templateUrl: './brand.component.html',
  styleUrls: ['./brand.component.css']
})
export class BrandComponent implements OnInit {

  filter

  state


  states = [
    "LIKE",
    "HOPE",
    "MARK",
    "UNCHECKED",
    "BLOCK"
  ]

  constructor(private route: ActivatedRoute) {
  }

  onStateSelected() {

    const state = BrandStates[`${this.state}`];

    console.log(state);
    const id = state.value

    this.filter = JSON.stringify({"type": parseInt(id)})
  }

  ngOnInit(): void {
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
