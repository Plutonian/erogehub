import {Component, Input, OnChanges, SimpleChanges} from '@angular/core';
import {StarGroupItem} from "../../../../entity";
import {GameService} from "../../../game.service";

@Component({
  selector: 'app-group-star',
  templateUrl: './group-star.component.html',
  styleUrls: ['./group-star.component.css']
})
export class GroupStarComponent implements OnChanges {

  @Input()
  filter

  starGroup: StarGroupItem[]

  constructor(private service: GameService) {
  }

  ngOnChanges(changes: SimpleChanges): void {

    const filter = this.filter

    if (filter) {
      this.service.groupByStar(filter)
        .subscribe((data: StarGroupItem[]) => this.starGroup = data)

    }
  }

}
