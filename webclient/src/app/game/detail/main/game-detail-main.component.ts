import {Component, Input, OnInit} from '@angular/core';
import {Game} from "../../../entity";

@Component({
  selector: 'app-game-detail-main',
  templateUrl: './game-detail-main.component.html',
  styleUrls: ['./game-detail-main.component.css']
})
export class GameDetailMainComponent implements OnInit {

  @Input()
  game: Game

  constructor() { }

  ngOnInit(): void {
  }

}
