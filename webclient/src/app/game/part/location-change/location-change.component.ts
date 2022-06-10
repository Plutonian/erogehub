import {Component, Input, OnInit} from '@angular/core';
import {Game} from "../../../entity";
import {GameLocation, GameService} from "../../game.service";

@Component({
  selector: 'app-location-change',
  templateUrl: './location-change.component.html',
  styleUrls: ['./location-change.component.css']
})
export class LocationChangeComponent implements OnInit {
  @Input()
  game: Game

  edit: Boolean = false

  locations = [
    "LOCAL",
    "REMOTE"
  ]

  change() {
    this.edit = !this.edit
  }

  onStateSelected() {
    this.change()

    const location = GameLocation[`${this.game.location}`];
    console.log(location);

    this.gameService.changeLocation(this.game.id, location.value)
      .subscribe((data: string) =>
        console.log(data)
      )
  }

  constructor(private gameService: GameService) {
  }

  ngOnInit(): void {
    // this.selectState = this.game.state
  }


}
