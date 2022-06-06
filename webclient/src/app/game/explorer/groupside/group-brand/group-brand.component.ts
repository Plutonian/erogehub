import {Component, Input, OnChanges, SimpleChanges} from '@angular/core';
import {BrandGroupItem} from "../../../../entity";
import {GameService} from "../../../game.service";

@Component({
  selector: 'app-group-brand',
  templateUrl: './group-brand.component.html',
  styleUrls: ['./group-brand.component.css']
})
export class GroupBrandComponent implements OnChanges {
  @Input()
  filter

  hidden = true

  brandGroup: BrandGroupItem[]


  constructor(private service: GameService) {
  }

  change(child: HTMLElement) {
    child.hidden = !child.hidden
  }


  ngOnChanges(changes: SimpleChanges): void {

    const filter = this.filter

    if (filter != null) {
      this.service.groupByBrand(filter)
        .subscribe((data: BrandGroupItem[]) => this.brandGroup = data)

    }

  }

}
