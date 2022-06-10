import { Component, OnInit } from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {Title} from "@angular/platform-browser";

@Component({
  selector: 'app-by-painter',
  templateUrl: './by-painter.component.html',
  styleUrls: ['./by-painter.component.css']
})
export class ByPainterComponent implements OnInit {

  filter: string

  constructor(private route: ActivatedRoute,
              private titleService: Title
              ) {
  }

  ngOnInit(): void {

    this.route.params.subscribe(p => {

      // @ts-ignore
      if (p.painter != null) {
        this.titleService.setTitle(`Painter: ${p['painter']}`)

        // @ts-ignore
        const filter = {'painter': p.painter}

        this.filter = JSON.stringify(filter)
      }

    })
  }


}
