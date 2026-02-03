import {Component, EventEmitter, Input, Output} from '@angular/core';
import {Brand, Emotion} from "../../entity";

@Component({
    selector: 'app-aggregate-game',
    templateUrl: './aggregate-game.component.html',
    styleUrls: ['./aggregate-game.component.css'],
    standalone: false
})
export class AggregateGameComponent {

    @Input()
    filter: string

    @Output()
    onBrandSelected = new EventEmitter<Brand>()

    @Output()
    onTagSelect = new EventEmitter<string>()

    @Output()
    onEmotionSelected = new EventEmitter<Emotion>()

    @Output()
    onLocationSelected = new EventEmitter<string>()

    @Output()
    onCVSelect = new EventEmitter<string>()

    @Output()
    onStarSelect = new EventEmitter<number>()

}
