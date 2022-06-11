import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {Title} from "@angular/platform-browser";

@Component({
  selector: 'app-by-truecv',
  templateUrl: './by-truecv.component.html',
  styleUrls: ['./by-truecv.component.css']
})
export class ByTruecvComponent implements OnInit {

  filter: string

  constructor(private route: ActivatedRoute, private titleService: Title) {
  }

  ngOnInit(): void {

    this.route.params.subscribe(p => {

      // @ts-ignore
      const truecv = p.truecv


      if (truecv) {
        this.titleService.setTitle(`${truecv}`)

        const filter = {'gamechar.truecv': truecv}

        this.filter = JSON.stringify(filter)
      }

    })
  }

}
