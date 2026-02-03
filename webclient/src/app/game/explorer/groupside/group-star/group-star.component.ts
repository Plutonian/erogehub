import {Component, EventEmitter, Input, Output} from '@angular/core';
import {StarGroupItem} from "../../../../entity";

@Component({
    selector: 'app-group-star',
    templateUrl: './group-star.component.html',
    styleUrls: ['./group-star.component.css'],
    standalone: false
})
export class GroupStarComponent {

  @Input()
  starGroup: StarGroupItem[]

  @Output()
  onStarSelect = new EventEmitter<number>()

  selectStar(star: number) {
    console.log(star);
    this.onStarSelect.emit(star)
  }

}
