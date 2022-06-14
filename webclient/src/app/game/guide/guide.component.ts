import {Component, Input, OnInit} from '@angular/core';
import {NgForm} from "@angular/forms";
import {ActivatedRoute} from "@angular/router";
import {GuideService} from "../guide.service";
import {Guide} from "../../entity";

@Component({
  selector: 'app-guide',
  templateUrl: './guide.component.html',
  styleUrls: ['./guide.component.css']
})
export class GuideComponent implements OnInit {

  @Input()
  searchKey: string

  guideList: Guide[]

  constructor(private route: ActivatedRoute,
              private guideService: GuideService) {
  }

  ngOnInit(): void {

    // this.route.params.subscribe(p => {
    // @ts-ignore
    const searchKey: string = this.searchKey

    if (searchKey != null) {
      console.log('PARAM: ', searchKey);
      this.searchKey = searchKey

      this.guideService.search(this.searchKey)
        .subscribe((data: Guide[]) => this.guideList = data)
    }
    // })
  }

  onSubmit(form: NgForm) {

    this.guideService.search(this.searchKey)
      .subscribe((data: Guide[]) => this.guideList = data)
  }

}
