import {Component, Input, OnInit} from '@angular/core';
import {Game, Location} from "../../../entity";
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
    Location.LOCAL,
    Location.REMOTE
  ]

  protected readonly Location = Location

  onLocationSelected() {

    const location = this.game._location as Location;
    console.log(location);

    this.gameService.changeLocation(this.game.id, location as number)
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
