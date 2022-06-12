import {Component, EventEmitter, Input, OnChanges, Output, SimpleChanges} from '@angular/core';
import {Brand, CVGroupItem, DateGroupItem, EmotionGroupItem, StarGroupItem, TagGroupItem} from "../../../entity";
import {GameService} from "../../game.service";

@Component({
  selector: 'app-groupside',
  templateUrl: './groupside.component.html',
  styleUrls: ['./groupside.component.css']
})
export class GroupsideComponent implements OnChanges {

  @Input()
  filter

  // @Output()
  // onBrandSelected = new EventEmitter<Brand>()

  @Output()
  onEmotionSelected = new EventEmitter<string>()

  dateGroup: DateGroupItem[]

  cvGroup: CVGroupItem[]

  tagGroup: TagGroupItem[]

  starGroup: StarGroupItem[]

  emotionGroup: EmotionGroupItem[]

  constructor(private service: GameService) {
  }

  ngOnChanges(changes: SimpleChanges): void {

    const filter = this.filter

    if (filter) {
      this.service.groupByDate(filter)
        .subscribe((data: DateGroupItem[]) => this.dateGroup = data)

      this.service.groupByCV(filter)
        .subscribe((data: CVGroupItem[]) => this.cvGroup = data)

      this.service.groupByTag(filter)
        .subscribe((data: TagGroupItem[]) => this.tagGroup = data)

      this.service.groupByStar(filter)
        .subscribe((data: StarGroupItem[]) => this.starGroup = data)

      this.service.groupByEmotion(filter)
        .subscribe((data: EmotionGroupItem[]) => this.emotionGroup = data)
    }
  }

  // brandSelected(brand: Brand) {
  //   this.onBrandSelected.emit(brand)
  // }


  emotionSelect(item: EmotionGroupItem) {
    this.onEmotionSelected.emit(item.emotion)
  }

}
