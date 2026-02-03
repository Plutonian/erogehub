import {Component, EventEmitter, Input, Output} from '@angular/core';
import {LocationGroupItem} from "../../../../entity";

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
  onLocationSelected = new EventEmitter<string>()

  locationSelect(item: LocationGroupItem) {
    this.onLocationSelected.emit(item.location)
  }
}
