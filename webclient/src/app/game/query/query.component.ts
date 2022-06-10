import {Component, OnInit} from '@angular/core';
import {GameService} from "../game.service";
import {ActivatedRoute} from "@angular/router";
import {Title} from "@angular/platform-browser";

@Component({
  selector: 'app-query',
  templateUrl: './query.component.html',
  styleUrls: ['./query.component.css']
})
export class QueryComponent implements OnInit {

  filter

  constructor(private service: GameService, private route: ActivatedRoute,
              private titleService: Title,) {
  }

  ngOnInit(): void {


    this.route.params.subscribe(p => {
      // @ts-ignore
      const filter: string = p.filter

      if (filter != null) {
        this.filter = filter

        this.titleService.setTitle(`Query:${filter}`)
      }
    })
  }

}
