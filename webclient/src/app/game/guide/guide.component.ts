import {Component, OnInit} from '@angular/core';
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

  // @Input()
  searchKey: String

  guideList: Guide[]

  constructor(private route: ActivatedRoute,
              private guideService: GuideService) {
  }

  ngOnInit(): void {

    this.route.params.subscribe(p => {
      // @ts-ignore
      const searchKey: String = p.searchKey

      if (searchKey != null) {
        console.log('PARAM: ', searchKey);
        this.searchKey = searchKey
      }
    })
  }

  onSubmit(form: NgForm) {
    // const searchKey = form.value.searchKey;

    // console.log(searchKey)

    this.guideService.search(this.searchKey)
      .subscribe((data: Guide[]) => this.guideList = data)

    // const v1 = {"name": {"$regex": `^${searchKey}`}}
    // const v2 = {"name": {"$regex": `${searchKey}`}}
    //
    // let filter
    //
    // if (searchType == "0") {
    //   filter=v1
    // } else {
    //   filter=v2
    // }

    // this.filter=JSON.stringify(filter)

    // this.gameService.query(JSON.stringify(filter))
    //   .subscribe((gs: Game[]) => this.gamelist = gs)

  }

}
