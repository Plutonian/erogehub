import {Component, EventEmitter, Input, Output} from '@angular/core';
import {
    Brand,
    Emotion,
    EmotionGroupItem,
    TagGroupItem,
    BrandGroupItem,
    CVGroupItem,
    StarGroupItem,
    LocationGroupItem,
    DateGroupItem,
    Location,
    PlayState,
    PlayStateGroupItem
} from "../../entity";

@Component({
    selector: 'app-aggregate-game',
    templateUrl: './aggregate-game.component.html',
    styleUrls: ['./aggregate-game.component.css'],
    standalone: false
})
export class AggregateGameComponent {

    @Input()
    emotionGroup: EmotionGroupItem[]

    @Input()
    tagGroup: TagGroupItem[]

    @Input()
    brandGroup: BrandGroupItem[]

    @Input()
    cvGroup: CVGroupItem[]

    @Input()
    starGroup: StarGroupItem[]

    @Input()
    locationGroup: LocationGroupItem[]

    @Input()
    dateGroup: DateGroupItem[]

    @Input()
    playStateGroup: PlayStateGroupItem[]

    @Output()
    onBrandSelected = new EventEmitter<Brand>()

    @Output()
    onTagSelect = new EventEmitter<string>()

    @Output()
    onEmotionSelected = new EventEmitter<Emotion>()

    @Output()
    onLocationSelected = new EventEmitter<Location>()

    @Output()
    onCVSelect = new EventEmitter<string>()

    @Output()
    onStarSelect = new EventEmitter<number>()

    @Output()
    onDateSelect = new EventEmitter<{start: string, end: string}>()

    @Output()
    onPlayStateSelected = new EventEmitter<PlayState>()

}
