import {Component, Input, OnInit} from '@angular/core';
import {Game} from "../../../../entity";

@Component({
  selector: 'app-game-list-panel',
  templateUrl: './game-list-panel.component.html',
  styleUrls: ['./game-list-panel.component.css']
})
export class GameListPanelComponent implements OnInit {

  @Input()
  games: Game[]




  constructor() {
  }

  ngOnInit(): void {
  }

}
