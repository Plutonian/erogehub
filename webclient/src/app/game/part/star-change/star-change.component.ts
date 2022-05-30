import {Component, Input, OnInit} from '@angular/core';
import {Game} from "../../../entity";
import {GameService} from "../../game.service";

@Component({
  selector: 'app-star-change',
  templateUrl: './star-change.component.html',
  styleUrls: ['./star-change.component.css']
})
export class StarChangeComponent implements OnInit {
  @Input()
  game: Game

  ngOnInit(): void {
  }


  constructor(private gameService: GameService) {
  }

  starChange(): void {

    console.log("change")
    console.log(this.game.star);

    this.gameService.changeStar(this.game.id, this.game.star)
      .subscribe((data) => console.log(data))
  }

}
