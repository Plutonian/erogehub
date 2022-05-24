import {Component, Input, OnInit} from '@angular/core';
import {Arrays} from "../../../../main";
import {Game} from "../../../entity";

@Component({
  selector: 'app-info',
  templateUrl: './info.component.html',
  styleUrls: ['./info.component.css']
})
export class InfoComponent implements OnInit {

  @Input()
  game: Game

  // @ts-ignore
  stars = Arrays.range(0, this.game?.star)

  imgUrl() {
    return `http://192.168.2.236/game/${this.game?.publishDate?.year}/${this.game?.publishDate?.monthValue}/${this.game?.id}`
  }

  fillState() {
    return this.game?.location === "LOCAL" ? "green" : "red"
  }

  constructor() {
  }

  ngOnInit(): void {
  }

}
