import {Component, EventEmitter, Input, Output, OnChanges, SimpleChanges} from '@angular/core';
import {Brand, BrandGroupItem} from "../../../../entity";
import {NzFormatEmitEvent} from "ng-zorro-antd/tree";

@Component({
    selector: 'app-group-brand',
    templateUrl: './group-brand.component.html',
    styleUrls: ['./group-brand.component.css'],
    standalone: false
})
export class GroupBrandComponent implements OnChanges {
  @Input()
  brandGroup: BrandGroupItem[]

  @Output()
  onBrandSelected = new EventEmitter<Brand>()

  data

  ngOnChanges(changes: SimpleChanges): void {
    if (this.brandGroup) {
      this.data = this.brandGroup.map(item => this.makeTree(item))
    }
  }

  nodeSelected(item: NzFormatEmitEvent) {
    if (item.node.isLeaf) {
      this.onBrandSelected.emit(item.node?.origin["item"]?.brand)
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
