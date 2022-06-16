import {Component, Input, OnChanges, SimpleChanges} from '@angular/core';
import {DateGroupItem} from "../../../../entity";
import {GameService} from "../../../game.service";

@Component({
  selector: 'app-group-date',
  templateUrl: './group-date.component.html',
  styleUrls: ['./group-date.component.css']
})
export class GroupDateComponent implements OnChanges {

  @Input()
  filter

  dateGroup: DateGroupItem[]

  constructor(private service: GameService) {
  }

  ngOnInit(): void {
  }

  ngOnChanges(changes: SimpleChanges): void {

    const filter = this.filter

    if (filter) {
      this.service.groupByDate(filter)
        .subscribe((data: DateGroupItem[]) => this.dateGroup = data)

    }
  }

}
