import {Component, Input, OnInit} from '@angular/core';
import {Game, GameCharacter} from "../../../../entity";
import {GameService} from "../../../game.service";
import {NzMessageService} from "ng-zorro-antd/message";

@Component({
  selector: 'app-game-char-cell',
  templateUrl: './game-char-cell.component.html',
  styleUrls: ['./game-char-cell.component.css']
})
export class GameCharCellComponent implements OnInit {
  @Input()
  game: Game

  title

  subTitle

  @Input()
  character: GameCharacter

  isVisible: boolean

  constructor(private gameService: GameService,
              private message: NzMessageService
  ) {
  }


  showModal(): void {
    this.isVisible = true;
  }

  handleCancel() {
    this.isVisible = false;
  }

  updateCV() {

    this.gameService.setCharCV(this.game.id, this.character.index, this.character.cv).subscribe((data: string) => {
      this.message.create("success", data)
    })
  }

  clearCV() {
    this.gameService.clearCharCV(this.game.id, this.character.index).subscribe((data: string) => {
      this.character.cv = null
      this.character.trueCV = null
      this.message.create("success", data)
    })
  }

  setMan() {
    this.gameService.setMan(this.game.id, this.character.index).subscribe((data: string) => {
      this.isVisible = false
      this.character.heroine = false
      this.message.create("success", data)
    })
  }

  ngOnInit(): void {

    const temp = this.character?.name?.split(/\s/)

    if (temp.length > 1) {
      this.subTitle = temp[0]
      this.title = temp[1]
    } else {
      this.title = this.character?.name
    }
  }

}
