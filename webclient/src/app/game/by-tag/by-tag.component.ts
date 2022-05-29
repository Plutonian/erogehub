import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";

@Component({
  selector: 'app-by-tag',
  templateUrl: './by-tag.component.html',
  styleUrls: ['./by-tag.component.css']
})
export class ByTagComponent implements OnInit {

  filter: String

  constructor(private route: ActivatedRoute) {
  }

  ngOnInit(): void {

    this.route.params.subscribe(p => {

      // @ts-ignore
      if (p.tag != null) {
        // @ts-ignore
        const filter = {'tag': p.tag}

        this.filter = JSON.stringify(filter)
      }

    })
  }
}
