import {Component, Input, OnInit, TemplateRef, ViewChild} from '@angular/core';
import {Game} from "../../entity";
import {DrawerService} from "ng-devui";
import {GameService} from "../game.service";

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

  constructor(private drawerService: DrawerService, private gameService: GameService) {

  }

  ngOnInit(): void {


  }

  @ViewChild('drawerContent', {static: true})
  drawerContent: TemplateRef<any>;

  openDrawer() {
    this.drawerService.open({
      width: '1500px',
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
      .subscribe((data) => console.log(data))

  }

  // close($event) {
  //   this.results.drawerInstance.hide();
  // }

}
