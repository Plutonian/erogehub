import {Component, EventEmitter, Input, Output} from '@angular/core';
import {Location, LocationGroupItem} from "../../../../entity";

@Component({
    selector: 'app-group-location',
    templateUrl: './group-location.component.html',
    styleUrls: ['./group-location.component.css'],
    standalone: false
})
export class GroupLocationComponent {

  @Input()
  locationGroup: LocationGroupItem[]

  @Output()
  onLocationSelected = new EventEmitter<Location>()

  locationSelect(item: LocationGroupItem) {
    // 处理空值，默认使用 Location.REMOTE
    this.onLocationSelected.emit(item.location  as Location);
  }
}
