import {Component, Input, OnInit} from '@angular/core';
import {Game} from "../../entity";
import {Arrays} from "../../../main";
import {Router} from "@angular/router";

@Component({
  selector: 'app-grid-cell',
  templateUrl: './grid-cell.component.html',
  styleUrls: ['./grid-cell.component.css']
})
export class GridCellComponent implements OnInit {

  @Input()
  game: Game

  // @ts-ignore
  stars = Arrays.range(0, this.game?.star)

  constructor() {

  }

  ngOnInit(): void {
  }

}
