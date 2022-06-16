import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {Game} from "../../../entity";
import {GameService} from "../../game.service";
import {Title} from "@angular/platform-browser";
import {DataQuery} from "../DataQuery";

@Component({
  selector: 'app-by-tag',
  templateUrl: './by-tag.component.html',
  styleUrls: ['./by-tag.component.css']
})
export class ByTagComponent extends DataQuery implements OnInit {

  tag: string

  constructor(private route: ActivatedRoute,
              private service: GameService,
              private titleService: Title,
  ) {
    super()
  }

  ngOnInit(): void {

    this.route.params.subscribe(p => {
      // @ts-ignore
      this.tag = p.tag


      if (this.tag != null) {
        const filter = {'tag': this.tag}

        this.titleService.setTitle(this.tag)

        this.filter = JSON.stringify(filter)


        this.service.query(this.filter)
          .subscribe((gs: Game[]) => {

              this.gamelist = gs
              this.rowGameList = gs

              console.log("Gs", gs);

            }
          )
      }

    })
  }
}
