import {Component, Input, OnInit} from '@angular/core';
import {Game, Location} from "../../../entity";

@Component({
    selector: 'app-location-show',
    templateUrl: './location-show.component.html',
    styleUrls: ['./location-show.component.css'],
    standalone: false
})
export class LocationShowComponent implements OnInit {

  @Input()
  game: Game

  constructor() { }

  fillLocation() {
    return this.game?._location === Location.LOCAL ? "green" : "red"
  }

  ngOnInit(): void {
  }

}
