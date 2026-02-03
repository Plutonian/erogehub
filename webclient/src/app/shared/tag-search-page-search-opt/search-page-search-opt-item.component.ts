import {Component, EventEmitter, Input, Output} from '@angular/core';
import {SearchOpt,SEARCHTYPE,FILTER,Emotion as EmotionEnum,PlayState,Location} from "../../entity";

@Component({
    selector: 'app-search-page-search-opt-item',
    templateUrl: './search-page-search-opt-item.component.html',
    standalone:false,
    styleUrl: './search-page-search-opt-item.component.css'
})
export class SearchPageSearchOptItemComponent {

    @Input()
    searchOpt: SearchOpt

    @Output()
    onRemove = new EventEmitter<SearchOpt>()

    @Output()
    onSwap = new EventEmitter<SearchOpt>()

    readonly SEARCHTYPE = SEARCHTYPE
    readonly Emotion = EmotionEnum
    readonly PlayState = PlayState
    readonly Location = Location

    swap(){
        this.onSwap.emit(this.searchOpt)
    }

    remove() {
        this.onRemove.emit(this.searchOpt)
    }

    isStarInput(key: SEARCHTYPE): boolean {
        return key === SEARCHTYPE.STAR
    }

    isSelectInput(key: SEARCHTYPE): boolean {
        return key === SEARCHTYPE.EMOTION || key === SEARCHTYPE.LOCATION || key === SEARCHTYPE.PLAYSTATE
    }

    isTextInput(key: SEARCHTYPE): boolean {
        return !this.isStarInput(key) && !this.isSelectInput(key)
    }

    protected readonly FILTER = FILTER;
}
