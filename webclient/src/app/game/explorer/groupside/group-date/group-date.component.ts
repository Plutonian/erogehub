import {Component, EventEmitter, Input, Output} from '@angular/core';
import {DateGroupItem} from "../../../../entity";

@Component({
    selector: 'app-group-date',
    templateUrl: './group-date.component.html',
    styleUrls: ['./group-date.component.css'],
    standalone: false
})
export class GroupDateComponent {

  @Input()
  dateGroup: DateGroupItem[]

  @Output()
  onDateSelect = new EventEmitter<{start: string, end: string}>()

}
