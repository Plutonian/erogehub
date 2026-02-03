import {Component, EventEmitter, Input, Output} from '@angular/core';
import {TagGroupItem} from "../../../../entity";

@Component({
    selector: 'app-group-tag',
    templateUrl: './group-tag.component.html',
    styleUrls: ['./group-tag.component.css'],
    standalone: false
})
export class GroupTagComponent {

  @Input()
  tagGroup: TagGroupItem[]

  @Output()
  onTagSelect = new EventEmitter<string>()

  select(tag: string) {
    this.onTagSelect.emit(tag)
  }

}
