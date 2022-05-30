import {Component, Input, OnInit} from '@angular/core';
import {Game} from "../../entity";

@Component({
  selector: 'app-list-cell',
  templateUrl: './list-cell.component.html',
  styleUrls: ['./list-cell.component.css']
})
export class ListCellComponent implements OnInit {
  @Input()
  game: Game


  constructor() {

  }

  ngOnInit(): void {

  }

}
