import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Game} from "../../../../entity";
import {GameService} from "../../../game.service";

@Component({
  selector: 'app-grid-cell',
  templateUrl: './grid-cell.component.html',
  styleUrls: ['./grid-cell.component.css']
})
export class GridCellComponent implements OnInit {

  @Input()
  game: Game

  @Input()
  large = true

  @Output()
  remove = new EventEmitter<Game>();

  constructor(private gameService: GameService) {

  }

  ngOnInit(): void {


  }

  delete() {
    this.gameService.delete(this.game.id)
      .subscribe((data) => {
        console.log(data)
        this.remove.emit(this.game)
      })

  }

}
