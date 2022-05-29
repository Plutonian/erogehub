import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {GameStates} from "../game.service";

@Component({
  selector: 'app-by-state',
  templateUrl: './by-state.component.html',
  styleUrls: ['./by-state.component.css']
})
export class ByStateComponent implements OnInit {
  filter: String

  constructor(private route: ActivatedRoute) {
  }

  ngOnInit(): void {

    this.route.params.subscribe(p => {

      // @ts-ignore
      if (p.state != null) {


        // @ts-ignore
        const filter = {'state': GameStates[p.state].value}

        this.filter = JSON.stringify(filter)
      }

    })
  }

}
