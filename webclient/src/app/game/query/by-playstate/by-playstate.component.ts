import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {Title} from "@angular/platform-browser";
import {GamePlayStates} from "../../game.service";

@Component({
  selector: 'app-by-playstate',
  templateUrl: './by-playstate.component.html',
  styleUrls: ['./by-playstate.component.css']
})
export class ByPlaystateComponent implements OnInit {
  filter: String

  constructor(private route: ActivatedRoute,
              private titleService: Title,
  ) {
  }

  ngOnInit(): void {

    this.route.params.subscribe(p => {

      // @ts-ignore
      if (p.playState != null) {

        // this.titleService.setTitle(`Query By playState:${p['playState']}`)
        // @ts-ignore
        const filter = {'playState': GamePlayStates[p.playState].value}

        this.filter = JSON.stringify(filter)
      }

    })
  }

}
