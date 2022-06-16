import {Component, EventEmitter, Input, OnChanges, Output, SimpleChanges} from '@angular/core';
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

  @Output()
  onStarSelect = new EventEmitter<number>()

  starGroup: StarGroupItem[]

  constructor(private service: GameService) {
  }

  ngOnChanges(changes: SimpleChanges): void {

    const filter = this.filter

    if (filter) {
      this.service.groupByStar(filter)
        .subscribe((data: StarGroupItem[]) => this.starGroup = data.reverse())

    }
  }

  selectStar(star: number) {
    console.log(star);
    this.onStarSelect.emit(star)
  }

}
