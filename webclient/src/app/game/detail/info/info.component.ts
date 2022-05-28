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


  constructor() {
  }

  ngOnInit(): void {
  }

}
