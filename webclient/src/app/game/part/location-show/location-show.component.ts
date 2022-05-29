import {Component, Input, OnInit} from '@angular/core';
import {Game} from "../../../entity";

@Component({
  selector: 'app-location-show',
  templateUrl: './location-show.component.html',
  styleUrls: ['./location-show.component.css']
})
export class LocationShowComponent implements OnInit {

  @Input()
  game: Game

  constructor() { }

  fillLocation() {
    return this.game?.location === "LOCAL" ? "green" : "red"
  }

  ngOnInit(): void {
  }

}
