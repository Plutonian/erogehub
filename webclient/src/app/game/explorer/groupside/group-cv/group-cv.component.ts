import {Component, Input, OnChanges, SimpleChanges} from '@angular/core';
import {CVGroupItem} from "../../../../entity";
import {GameService} from "../../../game.service";

@Component({
  selector: 'app-group-cv',
  templateUrl: './group-cv.component.html',
  styleUrls: ['./group-cv.component.css']
})
export class GroupCvComponent implements OnChanges {


  @Input()
  filter

  cvGroup: CVGroupItem[]


  constructor(private service: GameService) {
  }

  ngOnChanges(changes: SimpleChanges): void {

    const filter = this.filter

    if (filter) {

      this.service.groupByCV(filter)
        .subscribe((data: CVGroupItem[]) => this.cvGroup = data)

    }
  }

}
