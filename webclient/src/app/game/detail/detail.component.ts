import {Component, ElementRef, Input, OnInit, ViewChild} from '@angular/core';
import {Game} from "../../entity";
import {GameService} from "../game.service";
import {ActivatedRoute} from "@angular/router";
import {Title} from "@angular/platform-browser";
import {NzMessageService} from "ng-zorro-antd/message";


@Component({
    selector: 'app-game-detail',
    templateUrl: './detail.component.html',
    styleUrls: ['./detail.component.css'],
    standalone: false
})
export class DetailComponent implements OnInit {

  // @ts-ignore
  @Input()
  game: Game = null


  inputVisible = false;
  inputValue = '';
  @ViewChild('inputElement', {static: false})
  inputElement?: ElementRef;


  modelVisible: boolean

  showModal(): void {
    this.modelVisible = true;
  }


  showInput(): void {
    this.inputVisible = true;
    setTimeout(() => {
      this.inputElement?.nativeElement.focus();
    }, 10);
  }

  handleClose(removedTag: string): void {
    this.game.tag = this.game.tag.filter(tag => tag !== removedTag);
    this.service.updateTags(this.game.id, this.game.tag)
        .subscribe((ret: string) => {
          this.message.create("success", ret)
        })

  }

  handleInputConfirm(): void {
    if (this.inputValue?.length > 0) {
      this.game.tag.push(this.inputValue)


      this.service.updateTags(this.game.id, this.game.tag)
          .subscribe((ret: string) => {
            this.message.create("success", ret)
          })

    }

    this.inputValue = '';
    this.inputVisible = false;
  }

  handleCancel() {
    this.modelVisible = false;
  }

  updateTitle() {
    this.service.setTitle(this.game.id, this.game.mainTitle, this.game.subTitle)
      .subscribe((data: string) => {
        this.message.create("success", data)
      })
  }

  splitTitle() {
    const spIndex = this.game.name.indexOf(" ")

    if (spIndex != -1) {
      this.game.mainTitle = this.game.name.substring(0, spIndex)
      this.game.subTitle = this.game.name.substring(spIndex + 1, this.game.name.length)
    }

  }


  constructor(private service: GameService, private route: ActivatedRoute,
              private titleService: Title,
              private message: NzMessageService
  ) {

  }

  private readonly today = new Date()

  published() {

    let date= this.game._publishDate

    return this.today.getTime() - date.getTime() > 0
  }

  ngOnInit(): void {

    if (this.game == null)
      this.route.params.subscribe(p => {

        // @ts-ignore
        this.service.info(p.id)
          .subscribe((game: Game) => {

            this.game = game

            // console.log(this.route.data["breadcrumb"]);
            this.titleService.setTitle(game.name)
          })

      })

  }

}





