import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Emotion, Game} from "../../../../entity";
import {GameService} from "../../../game.service";

@Component({
    selector: 'app-list-cell',
    templateUrl: './list-cell.component.html',
    styleUrls: ['./list-cell.component.css'],
    standalone: false
})
export class ListCellComponent implements OnInit {
  @Input()
  game: Game

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

    protected readonly Emotion = Emotion;
}
