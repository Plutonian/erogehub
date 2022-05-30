import {Component, Input, OnInit} from '@angular/core';
import {BrandGroupItem} from "../../../../entity";
import {GameService} from "../../../game.service";

@Component({
  selector: 'app-group-brand',
  templateUrl: './group-brand.component.html',
  styleUrls: ['./group-brand.component.css']
})
export class GroupBrandComponent implements OnInit {
  @Input()
  filter

  hidden=true

  brandGroup: BrandGroupItem[]


  constructor(private service: GameService) {
  }

  change(child: HTMLElement) {
    child.hidden = !child.hidden
  }


  ngOnInit(): void {

    const filter = this.filter

    this.service.groupByBrand(filter)
      .subscribe((data: BrandGroupItem[]) => this.brandGroup = data)

  }

}
