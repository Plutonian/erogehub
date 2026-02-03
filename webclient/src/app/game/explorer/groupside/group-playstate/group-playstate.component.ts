import {Component, EventEmitter, Input, Output} from '@angular/core';
import {PlayState, PlayStateGroupItem} from "../../../../entity";

@Component({
    selector: 'app-group-playstate',
    templateUrl: './group-playstate.component.html',
    styleUrls: ['./group-playstate.component.css'],
    standalone: false
})
export class GroupPlaystateComponent {

  @Input()
  playStateGroup: PlayStateGroupItem[]

  @Output()
  onPlayStateSelected = new EventEmitter<PlayState>()

  playStateSelect(item: PlayStateGroupItem) {
    this.onPlayStateSelected.emit(item.playState as PlayState);
  }
}
