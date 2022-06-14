import {Component, EventEmitter, Input, OnChanges, Output, SimpleChanges} from '@angular/core';
import {Brand, BrandGroupItem} from "../../../../entity";
import {GameService} from "../../../game.service";
import {NzFormatEmitEvent} from "ng-zorro-antd/tree";

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


  nodeSelected(item: NzFormatEmitEvent) {
    if (item.node.isLeaf) {
      this.onBrandSelected.emit(item.node?.origin["item"]?.brand)
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
      item: item,
      isLeaf: false
    }
    if (item.children && item.children.length > 0) {
      temp["children"] = item.children.map(sub => this.makeTree(sub))
    } else {
      temp.isLeaf = true
    }

    return temp
  }


}
