import {Component, Input, OnInit} from '@angular/core';
import {Game, MyDate} from "../../entity";

@Component({
  selector: 'app-formatted-date',
  templateUrl: './formatted-date.component.html',
  styleUrls: ['./formatted-date.component.css']
})
export class FormattedDateComponent implements OnInit {

  @Input()
  game: Game


  constructor() {
  }

  ngOnInit(): void {


  }


}
