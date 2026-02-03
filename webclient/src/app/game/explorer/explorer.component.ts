import {Component, Input, OnChanges, SimpleChanges} from '@angular/core';
import {Brand, Game} from "../../entity";
import {GameService} from "../game.service";
import {DataQuery} from "../query/DataQuery";


@Component({
    selector: 'app-explorer',
    templateUrl: './explorer.component.html',
    styleUrls: ['explorer.component.css'],
    standalone: false
})
export class ExplorerComponent extends DataQuery {

}
