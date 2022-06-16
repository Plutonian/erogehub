import {Component, EventEmitter, Input, OnChanges, Output, SimpleChanges} from '@angular/core';
import {EmotionGroupItem} from "../../../../entity";
import {GameService} from "../../../game.service";

@Component({
  selector: 'app-group-emotion',
  templateUrl: './group-emotion.component.html',
  styleUrls: ['./group-emotion.component.css']
})
export class GroupEmotionComponent implements OnChanges {

  @Input()
  filter: string

  @Output()
  onEmotionSelected = new EventEmitter<string>()


  emotionGroup: EmotionGroupItem[]

  constructor(private service: GameService) {
  }


  ngOnChanges(changes: SimpleChanges): void {

    const filter = this.filter

    if (filter) {

      this.service.groupByEmotion(filter)
        .subscribe((data: EmotionGroupItem[]) => this.emotionGroup = data)
    }
  }

  emotionSelect(item: EmotionGroupItem) {
    this.onEmotionSelected.emit(item.emotion)
  }

}
