import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {Emotions} from "../../../brand/brand.service";

@Component({
  selector: 'app-by-emotion',
  templateUrl: './by-emotion.component.html',
  styleUrls: ['./by-emotion.component.css']
})
export class ByEmotionComponent implements OnInit {

  filter: String

  constructor(private route: ActivatedRoute) {
  }

  ngOnInit(): void {

    this.route.params.subscribe(p => {

      // @ts-ignore
      if (p.emotion != null) {


        // @ts-ignore
        const filter = {'emotion': Emotions[p.emotion].value}

        this.filter = JSON.stringify(filter)
      }

    })
  }

}
