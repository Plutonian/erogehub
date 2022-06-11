import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {Title} from "@angular/platform-browser";

@Component({
  selector: 'app-by-cv',
  templateUrl: './by-cv.component.html',
  styleUrls: ['./by-cv.component.css']
})
export class ByCVComponent implements OnInit {
  filter: string

  constructor(private route: ActivatedRoute, private titleService: Title) {
  }

  ngOnInit(): void {

    this.route.params.subscribe(p => {
      // @ts-ignore
      const cv = p.cv

      if (cv) {
        this.titleService.setTitle(`${cv}`)

        const filter = {'gamechar.cv': cv}
        this.filter = JSON.stringify(filter)
      }

    })
  }

}
