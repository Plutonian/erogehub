import { Component, OnInit } from '@angular/core';
import {ActivatedRoute} from "@angular/router";

@Component({
  selector: 'app-by-cv',
  templateUrl: './by-cv.component.html',
  styleUrls: ['./by-cv.component.css']
})
export class ByCVComponent implements OnInit {
  filter: String

  constructor(private route: ActivatedRoute) {
  }

  ngOnInit(): void {

    this.route.params.subscribe(p => {

      // @ts-ignore
      if (p.cv != null) {
        // @ts-ignore
        const filter = {'gamechar.cv': p.cv}

        this.filter = JSON.stringify(filter)
      }

    })
  }

}
