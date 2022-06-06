import {Component, Input, OnInit, TemplateRef, ViewChild} from '@angular/core';
import {Game} from "../../entity";
import {GameService} from "../game.service";
import {DrawerService} from "ng-devui";

@Component({
  selector: 'app-list-cell',
  templateUrl: './list-cell.component.html',
  styleUrls: ['./list-cell.component.css']
})
export class ListCellComponent implements OnInit {
  @Input()
  game: Game


  constructor(private gameService: GameService, private drawerService: DrawerService) {

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

}
