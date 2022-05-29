import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {GameLocation} from "../game.service";

@Component({
  selector: 'app-by-location',
  templateUrl: './by-location.component.html',
  styleUrls: ['./by-location.component.css']
})
export class ByLocationComponent implements OnInit {
  filter: String

  constructor(private route: ActivatedRoute) {
  }

  ngOnInit(): void {

    this.route.params.subscribe(p => {

      // @ts-ignore
      if (p.location != null) {


        // @ts-ignore
        const filter = {'location': GameLocation[p.location].value}

        this.filter = JSON.stringify(filter)
      }

    })
  }
}
