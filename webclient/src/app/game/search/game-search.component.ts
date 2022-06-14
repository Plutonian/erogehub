import {Component, OnInit} from '@angular/core';

@Component({
  selector: 'app-gamesearch',
  templateUrl: './game-search.component.html',
  styleUrls: ['./game-search.component.css']
})
export class GameSearchComponent implements OnInit {

  filter

  searchType = 'Simple'

  searchKey: string


  constructor() {
  }

  ngOnInit(): void {
  }

  search() {

    // console.log(form);
    // const {searchKey} = form.value;
    //
    // console.log(searchKey)

    const v1 = {"name": {"$regex": `^${this.searchKey}`}}
    const v2 = {"name": {"$regex": `${this.searchKey}`}}

    let filter

    if (this.searchType == "Simple") {
      filter = v1
    } else {
      filter = v2
    }

    this.filter = JSON.stringify(filter)

  }

}
