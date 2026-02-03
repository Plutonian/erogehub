import {Component, Input, OnInit} from '@angular/core';
import {Game, GameLocation} from "../../../entity";
import {GameService} from "../../game.service";

@Component({
    selector: 'app-location-change',
    templateUrl: './location-change.component.html',
    styleUrls: ['./location-change.component.css'],
    standalone: false
})
export class LocationChangeComponent implements OnInit {
  @Input()
  game: Game

  edit: Boolean = false

  locations = [
    "LOCAL",
    "REMOTE"
  ]

  onLocationSelected() {

    const location = GameLocation[`${this.game._location}`];
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
