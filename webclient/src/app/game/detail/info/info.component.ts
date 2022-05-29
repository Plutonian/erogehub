import {Component, Input} from '@angular/core';
import {Game} from "../../../entity";
import {GameService} from "../../game.service";

@Component({
  selector: 'app-info',
  templateUrl: './info.component.html',
  styleUrls: ['./info.component.css']
})
export class InfoComponent {

  @Input()
  game: Game


  constructor(private gameService: GameService) {
  }

  starChange(): void {

    console.log("change")
    console.log(this.game.star);

    this.gameService.changeStar(this.game.id, this.game.star)
      .subscribe((data) => console.log(data))
  }


}
