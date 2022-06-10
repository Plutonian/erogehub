import {Component, EventEmitter, Input, OnChanges, Output, SimpleChanges} from '@angular/core';
import {Brand, BrandGroupItem} from "../../../../entity";
import {GameService} from "../../../game.service";
import {ITreeItem} from "ng-devui";

@Component({
  selector: 'app-group-brand',
  templateUrl: './group-brand.component.html',
  styleUrls: ['./group-brand.component.css']
})
export class GroupBrandComponent implements OnChanges {
  @Input()
  filter

  @Output()
  onBrandSelected = new EventEmitter<Brand>()

  data

  constructor(private service: GameService) {
  }

  change(child: HTMLElement) {
    child.hidden = !child.hidden
  }

  nodeSelected(item: ITreeItem) {
    // console.dir(item);
    if (!item.data.isParent) {
      this.onBrandSelected.emit(item.data?.originItem?.brand)
    }

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
    let temp = {
      // id: item.brand.id,
      // "title": `${item.title} (${item.count})`,
      "open": false,
      brand: item.brand
      // disabled: true
    }
    if (item.children && item.children.length > 0) {
      temp["items"] = item.children.map(sub => this.makeTree(sub))
      temp["title"] = `${item.title} (${item.count})`
    } else {
      temp["brandId"] = item.brand.id
      temp["title"] = `[${item.brand.state}] ${item.title} (${item.count})`
    }

    return temp
  }


}
