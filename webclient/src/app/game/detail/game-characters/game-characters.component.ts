import {Component, Input, OnInit} from '@angular/core';
import {Game} from "../../../entity";
import {Router} from "@angular/router";

@Component({
  selector: 'app-game-characters',
  templateUrl: './game-characters.component.html',
  styleUrls: ['./game-characters.component.css']
})
export class GameCharactersComponent implements OnInit {

  @Input()
  game: Game

  ngOnInit(): void {
  }

}
