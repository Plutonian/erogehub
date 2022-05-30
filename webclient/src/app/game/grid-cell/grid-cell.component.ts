import {Component, Input, OnInit} from '@angular/core';
import {Game} from "../../entity";

@Component({
  selector: 'app-grid-cell',
  templateUrl: './grid-cell.component.html',
  styleUrls: ['./grid-cell.component.css']
})
export class GridCellComponent implements OnInit {

  @Input()
  game: Game

  @Input()
  large = true

  constructor() {

  }

  ngOnInit(): void {


  }

}
