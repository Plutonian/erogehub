import {Component, OnInit} from '@angular/core';
import {GameService} from "../game.service";
import {ActivatedRoute} from "@angular/router";

@Component({
  selector: 'app-query',
  templateUrl: './query.component.html',
  styleUrls: ['./query.component.css']
})
export class QueryComponent implements OnInit {

  filter

  constructor(private service: GameService, private route: ActivatedRoute) {
  }

  ngOnInit(): void {
    this.route.params.subscribe(p => {
      // @ts-ignore
      const filter: string = p.filter

      if (filter != null) {
        this.filter = filter
      }
    })
  }

}
