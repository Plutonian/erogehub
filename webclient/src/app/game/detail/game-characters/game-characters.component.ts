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

  jumpRealCv(cv: String) {
    const filter = {"gamechar.truecv": cv}

    this.router.navigateByUrl(`/game/query/${JSON.stringify(filter)}`)
  }

  jumpCv(cv: String) {
    const filter = {"gamechar.cv": cv}

    this.router.navigateByUrl(`/game/query/${JSON.stringify(filter)}`)
  }

  constructor(private router: Router) {
  }

  ngOnInit(): void {
  }

}
