import {Component, EventEmitter, Input, OnInit, Output, TemplateRef, ViewChild} from '@angular/core';
import {DrawerService} from "ng-devui";
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

  constructor(private drawerService: DrawerService, private gameService: GameService) {

  }

  ngOnInit(): void {


  }

  @ViewChild('drawerContent', {static: true})
  drawerContent: TemplateRef<any>;

  openDrawer() {
    this.drawerService.open({
      width: '1800px',
      zIndex: 1000,
      isCover: true,
      fullScreen: true,
      backdropCloseable: true,
      escKeyCloseable: true,
      position: 'right',
      onClose: () => {
        console.log('on drawer closed');
      },
      contentTemplate: this.drawerContent
    });
  }

  delete() {
    this.gameService.delete(this.game.id)
      .subscribe((data) => {
        console.log(data)
        this.remove.emit(this.game)
      })

  }

  // close($event) {
  //   this.results.drawerInstance.hide();
  // }

}
