import {Component, Input, OnInit} from '@angular/core';
import {Brand} from "../../../entity";
import {Router} from "@angular/router";

@Component({
  selector: 'app-jump-brand',
  templateUrl: './jump-brand.component.html',
  styleUrls: ['./jump-brand.component.css']
})
export class JumpBrandComponent implements OnInit {
  @Input()
  brand: Brand

  constructor(private router: Router) {
  }

  ngOnInit(): void {
  }

  jump(brandId) {
    const filter = {"brandId": brandId}
    this.router.navigateByUrl(`/game/query/${JSON.stringify(filter)}`)
  }


}
