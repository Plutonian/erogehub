import {Component, Input, OnInit} from '@angular/core';
import {MyDate} from "../../entity";

@Component({
  selector: 'app-formatted-date',
  templateUrl: './formatted-date.component.html',
  styleUrls: ['./formatted-date.component.css']
})
export class FormattedDateComponent implements OnInit {

  @Input()
  date: MyDate

  _date: Date

  limit = 3 * 12 * 30 * 24 * 60 * 60; // tree years

  constructor() {
  }

  ngOnInit(): void {

    this._date = this.myDate2JSDate(this.date)
    console.log(this.date);
    console.log(typeof this.date);

  }

  myDate2JSDate(date: MyDate) {

    const newDate = new Date()
    // @ts-ignore
    newDate.setFullYear(date.year)
    // @ts-ignore
    newDate.setMonth(date.monthValue - 1)
    // @ts-ignore
    newDate.setDate(date.dayOfMonth)

    return newDate
  }

}
