import {Component, OnInit} from '@angular/core';
import {NgForm} from "@angular/forms";

@Component({
  selector: 'app-gamesearch',
  templateUrl: './game-search.component.html',
  styleUrls: ['./game-search.component.css']
})
export class GameSearchComponent implements OnInit {

  filter

  searchType = 'Simple'


  constructor() {
  }

  ngOnInit(): void {
  }

  onSubmit(form: NgForm) {
    const {searchKey} = form.value;

    console.log(searchKey)

    const v1 = {"name": {"$regex": `^${searchKey}`}}
    const v2 = {"name": {"$regex": `${searchKey}`}}

    let filter

    if (this.searchType == "Simple") {
      filter = v1
    } else {
      filter = v2
    }

    this.filter = JSON.stringify(filter)

  }

}
