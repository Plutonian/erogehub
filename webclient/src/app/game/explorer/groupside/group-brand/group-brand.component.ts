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

  // hidden = true

  // brandGroup: BrandGroupItem[]

  // @ViewChild('basicTree', {static: true})
  // basicTree: TreeComponent;

  data


  constructor(private service: GameService) {
  }

  change(child: HTMLElement) {
    child.hidden = !child.hidden
  }


  ngOnChanges(changes: SimpleChanges): void {

    const filter = this.filter

    if (filter != null) {
      this.service.groupByBrand(filter)
        .subscribe((brandGroupItems: BrandGroupItem[]) => {

          this.data = brandGroupItems.map(item => this.makeTree(item))
        })

    }

  }

  makeTree(item: BrandGroupItem) {
    let temp = {"title": `${item.title} [${item.count}]`, "open": false}
    if (item.children != null && item.children.length > 0) {
      temp["items"] = item.children.map(sub => this.makeTree(sub))
    }

    return temp
  }


}
