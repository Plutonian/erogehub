import {Component, EventEmitter, Input, Output} from '@angular/core';
import {CVGroupItem} from "../../../../entity";

@Component({
    selector: 'app-group-cv',
    templateUrl: './group-cv.component.html',
    styleUrls: ['./group-cv.component.css'],
    standalone: false
})
export class GroupCvComponent {

    @Input()
    cvGroup: CVGroupItem[]

    @Output()
    onCVSelected = new EventEmitter<string>()

    cvSelect(item: CVGroupItem) {
        this.onCVSelected.emit(item.title)
    }

}
