import {Component, Input, OnInit} from '@angular/core';
import {Game} from "../../../entity";

@Component({
  selector: 'app-game-characters',
  templateUrl: './game-characters.component.html',
  styleUrls: ['./game-characters.component.css']
})
export class GameCharactersComponent implements OnInit {

  @Input()
  game:Game

  imgUrl() {
    return `http://192.168.2.236/game/${this.game?.publishDate?.year}/${this.game?.publishDate?.monthValue}/${this.game?.id}`
  }

  constructor() { }

  ngOnInit(): void {
  }

}
