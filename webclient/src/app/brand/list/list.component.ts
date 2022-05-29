import {Component, Input, OnChanges, SimpleChanges} from '@angular/core';
import {Brand} from "../../entity";
import {BrandService} from "../brand.service";

@Component({
  selector: 'app-list',
  templateUrl: './list.component.html',
  styleUrls: ['./list.component.css']
})
export class BrandListComponent implements OnChanges {
  @Input()
  filter

  brands: Brand[]

  constructor(private service: BrandService) {
  }


  ngOnChanges(changes: SimpleChanges): void {

    // @ts-ignore
    const filter = changes.filter.currentValue

    console.log(`BrandListComponent :${filter}`);

    if (filter != null) {
      this.service.query(filter)
        .subscribe((gs: Brand[]) => {
            this.brands = gs

            console.log("Gs", gs);

          }
        )


    }

  }

}
