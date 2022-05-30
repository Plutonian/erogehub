import {Component, Input, OnInit} from '@angular/core';
import {CVGroupItem, DateGroupItem, TagGroupItem} from "../../../entity";
import {GameService} from "../../game.service";

@Component({
  selector: 'app-groupside',
  templateUrl: './groupside.component.html',
  styleUrls: ['./groupside.component.css']
})
export class GroupsideComponent implements OnInit {

  @Input()
  filter


  dateGroup: DateGroupItem[]

  cvGroup: CVGroupItem[]

  tagGroup: TagGroupItem[]

  constructor(private service: GameService) {
  }

  ngOnInit(): void {

    const filter = this.filter

    this.service.groupByDate(filter)
      .subscribe((data: DateGroupItem[]) => this.dateGroup = data)

    this.service.groupByCV(filter)
      .subscribe((data: CVGroupItem[]) => this.cvGroup = data)

    this.service.groupByTag(filter)
      .subscribe((data: TagGroupItem[]) => this.tagGroup = data)
  }

}
