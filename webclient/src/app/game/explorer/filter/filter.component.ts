import {Component, OnInit} from '@angular/core';
import {GroupItem} from "../../../entity";

export interface FilterItem {
  key: string
  expression: GroupItem
}


@Component({
  selector: 'app-filter',
  templateUrl: './filter.component.html',
  styleUrls: ['./filter.component.css']
})
export class FilterComponent implements OnInit {

  constructor() {
  }

  ngOnInit(): void {
  }

}
