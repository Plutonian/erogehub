import {Component, EventEmitter, Input, Output} from '@angular/core';
import {SearchOpt,SEARCHTYPE,FILTER} from "../../entity";

@Component({
    selector: 'app-search-page-search-opt-item',
    templateUrl: './search-page-search-opt-item.component.html',
    // imports: [
    //     NzSelectComponent,
    //     NzOptionComponent,
    //     NzSwitchComponent,
    //     NzInputGroupComponent,
    //     FormsModule,
    //     NzButtonComponent
    // ],
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

    swap(){
        this.onSwap.emit(this.searchOpt)
    }

    remove() {
        this.onRemove.emit(this.searchOpt)
    }

    protected readonly FILTER = FILTER;
}
