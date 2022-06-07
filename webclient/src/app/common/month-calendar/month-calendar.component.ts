import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {DateGroupItem} from "../../entity";


@Component({
  selector: 'app-month-calendar',
  templateUrl: './month-calendar.component.html',
  styleUrls: ['./month-calendar.component.css']
})
export class MonthCalendarComponent implements OnInit {

  @Input()
  months: DateGroupItem[]

  @Output()
  select = new EventEmitter<DateGroupItem>();

  constructor() {
  }

  ngOnInit(): void {
  }

  jump(date: DateGroupItem) {
    this.select.emit(date)
  }

}
