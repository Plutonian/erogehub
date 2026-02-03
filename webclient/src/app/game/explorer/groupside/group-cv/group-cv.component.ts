import {Component, EventEmitter, Input, OnChanges, Output, SimpleChanges} from '@angular/core';
import {CVGroupItem, EmotionGroupItem} from "../../../../entity";
import {GameService} from "../../../game.service";

@Component({
    selector: 'app-group-cv',
    templateUrl: './group-cv.component.html',
    styleUrls: ['./group-cv.component.css'],
    standalone: false
})
export class GroupCvComponent implements OnChanges {


    @Input()
    filter

    cvGroup: CVGroupItem[]

    @Output()
    onCVSelected = new EventEmitter<string>()


    constructor(private service: GameService) {
    }

    ngOnChanges(changes: SimpleChanges): void {

        const filter = this.filter

        if (filter) {

            this.service.groupByCV(filter)
                .subscribe((data: CVGroupItem[]) => this.cvGroup = data)

        }
    }

    cvSelect(item: CVGroupItem) {
        this.onCVSelected.emit(item.title)
    }

}
