import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";

@Component({
  selector: 'app-by-star',
  templateUrl: './by-star.component.html',
  styleUrls: ['./by-star.component.css']
})
export class ByStarComponent implements OnInit {

  filter: string

  constructor(private route: ActivatedRoute) {
  }

  ngOnInit(): void {

    this.route.params.subscribe(p => {

      // @ts-ignore
      if (p.star != null) {
        // @ts-ignore
        const filter = {'star': parseInt(p.star)}

        this.filter = JSON.stringify(filter)
      }

    })
  }

}
