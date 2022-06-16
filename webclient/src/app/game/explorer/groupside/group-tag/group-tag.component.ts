import {Component, EventEmitter, Input, OnChanges, Output, SimpleChanges} from '@angular/core';
import {TagGroupItem} from "../../../../entity";
import {GameService} from "../../../game.service";

@Component({
  selector: 'app-group-tag',
  templateUrl: './group-tag.component.html',
  styleUrls: ['./group-tag.component.css']
})
export class GroupTagComponent implements OnChanges {

  @Input()
  filter

  @Output()
  onTagSelect = new EventEmitter<string>()


  tagGroup: TagGroupItem[]


  constructor(private service: GameService) {
  }

  ngOnChanges(changes: SimpleChanges): void {

    const filter = this.filter

    if (filter) {

      this.service.groupByTag(filter)
        .subscribe((data: TagGroupItem[]) => this.tagGroup = data)
    }
  }

  select(tag: string) {
    this.onTagSelect.emit(tag)
  }

}
