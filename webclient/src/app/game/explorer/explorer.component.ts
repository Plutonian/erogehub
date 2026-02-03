import {Component, EventEmitter, Input, OnChanges, Output, SimpleChanges} from '@angular/core';
import {Brand, Game} from "../../entity";
import {GameService} from "../game.service";
import {DataQuery} from "../query/DataQuery";


@Component({
    selector: 'app-explorer',
    templateUrl: './explorer.component.html',
    styleUrls: ['explorer.component.css'],
    standalone: false
})
export class ExplorerComponent extends DataQuery{

    @Input()
    page: number = 1

    @Input()
    total: number = 0


    @Input()
    pageItemSize: number = 40


    @Output()
    pageChange = new EventEmitter<number>

    pageIndexChange(event: number) {
        this.pageChange.emit(event)
    }

}
