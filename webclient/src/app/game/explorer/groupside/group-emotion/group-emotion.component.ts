import {Component, EventEmitter, Input, Output} from '@angular/core';
import {EmotionGroupItem, Emotion} from "../../../../entity";

@Component({
    selector: 'app-group-emotion',
    templateUrl: './group-emotion.component.html',
    styleUrls: ['./group-emotion.component.css'],
    standalone: false
})
export class GroupEmotionComponent {

  @Input()
  emotionGroup: EmotionGroupItem[]

  @Output()
  onEmotionSelected = new EventEmitter<Emotion>()

  emotionSelect(item: EmotionGroupItem) {
    this.onEmotionSelected.emit(item.emotion)
  }

}
