import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {Title} from "@angular/platform-browser";

@Component({
  selector: 'app-by-date',
  templateUrl: './by-date.component.html',
  styleUrls: ['./by-date.component.css']
})
export class ByDateComponent implements OnInit {

  filter: string

  constructor(private route: ActivatedRoute,
              private titleService: Title
  ) {
  }

  ngOnInit(): void {

    this.route.params.subscribe(p => {
      // @ts-ignore
      const startStr = p.start
      // @ts-ignore
      const endStr = p.end

      if (startStr && endStr) {


        const start = Date.parse(`${startStr} 00:00:00`)
        const end = Date.parse(`${endStr} 23:59:59`)

        console.log(start, end)
        // console.log(start.getTime(), end.getTime())

        const filter = {"publishDate": {"$gte": {"$date": start}, "$lte": {"$date": end}}}

        // @ts-ignore
        console.log(p.start, p.end)
        this.titleService.setTitle(`From: ${startStr} to ${endStr}`)

        this.filter = JSON.stringify(filter)
      }

    })
  }

}
