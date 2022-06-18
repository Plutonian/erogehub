import {Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges} from '@angular/core';
import {EmotionGroupItem, LocationGroupItem} from "../../../../entity";
import {GameService} from "../../../game.service";

@Component({
  selector: 'app-group-location',
  templateUrl: './group-location.component.html',
  styleUrls: ['./group-location.component.css']
})
export class GroupLocationComponent implements OnChanges {

  @Input()
  filter: string

  @Output()
  onLocationSelected = new EventEmitter<string>()


  locationGroupItems: LocationGroupItem[]

  constructor(private service: GameService) {
  }


  ngOnChanges(changes: SimpleChanges): void {

    const filter = this.filter

    if (filter) {

      this.service.groupByLocation(filter)
        .subscribe((data: LocationGroupItem[]) => this.locationGroupItems = data)
    }
  }

  locationSelect(item: LocationGroupItem) {
    this.onLocationSelected.emit(item.location)
  }
}
