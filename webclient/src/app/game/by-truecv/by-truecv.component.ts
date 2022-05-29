import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";

@Component({
  selector: 'app-by-truecv',
  templateUrl: './by-truecv.component.html',
  styleUrls: ['./by-truecv.component.css']
})
export class ByTruecvComponent implements OnInit {

  filter: String

  constructor(private route: ActivatedRoute) {
  }

  ngOnInit(): void {

    this.route.params.subscribe(p => {

      // @ts-ignore
      if (p.truecv != null) {
        // @ts-ignore
        const filter = {'gamechar.truecv': p.truecv}

        this.filter = JSON.stringify(filter)
      }

    })
  }

}
