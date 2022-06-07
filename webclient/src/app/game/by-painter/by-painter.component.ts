import { Component, OnInit } from '@angular/core';
import {ActivatedRoute} from "@angular/router";

@Component({
  selector: 'app-by-painter',
  templateUrl: './by-painter.component.html',
  styleUrls: ['./by-painter.component.css']
})
export class ByPainterComponent implements OnInit {

  filter: string

  constructor(private route: ActivatedRoute) {
  }

  ngOnInit(): void {

    this.route.params.subscribe(p => {

      // @ts-ignore
      if (p.painter != null) {
        // @ts-ignore
        const filter = {'painter': p.painter}

        this.filter = JSON.stringify(filter)
      }

    })
  }


}
