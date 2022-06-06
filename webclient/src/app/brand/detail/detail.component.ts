import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {Brand, Emotion} from "../../entity";
import {BrandService, BrandStates} from "../brand.service";
import {AppService} from "../../app.service";

@Component({
  selector: 'app-brand-detail',
  templateUrl: './detail.component.html',
  styleUrls: ['./detail.component.css']
})
export class BrandDetailComponent implements OnInit {
  filter

  brand: Brand


  states: Emotion[]

  subBrands: Brand[]
  subBrandId: Number


  onSubBrandSelected() {

    console.log(this.subBrandId);

    this.router.navigateByUrl(`/brand/${this.subBrandId}`)
  }

  onStateSelected() {
    console.log(this.brand.state);

    // if (this.subBrands != null)
    //   this.subBrands.filter(brand => brand.id == this.brand.id).forEach(brand => {
    //     brand.state=this.brand.state
    //     brand.state.name = this.brand.state.name
    //     brand.state.value = this.brand.state.value
    //   })

    // const brandState = BrandStates[`${this.brand.state}`];

    this.brandService.changeState(this.brand.id, this.brand.state.value)
      .subscribe((data: string) => console.log(data))

  }


  constructor(private brandService: BrandService, private appService: AppService, private route: ActivatedRoute, private router: Router) {
  }

  ngOnInit(): void {
    this.appService.emotions().subscribe((data: Emotion[]) => {
      this.states = data
      // this.states = emotions.filter(data => data.value > 0)
    })


    this.route.params.subscribe(p => {
      // @ts-ignore
      const id = p.id

      if (id != null) {
        this.filter = JSON.stringify({"brandId": parseInt(id)})


        this.brandService.info(parseInt(id))
          .subscribe((data: Brand) => {
            this.brand = data

            if (this.brand?.comp != null) {

              this.brandService.query(JSON.stringify({"comp": `${this.brand.comp}`}))
                .subscribe((data: Brand[]) => {
                  this.subBrands = data

                  this.subBrandId = this.brand?.id
                })
            }

          })
      }
    })
  }

}
