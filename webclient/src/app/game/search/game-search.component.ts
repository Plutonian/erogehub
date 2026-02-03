import {Component, Input, OnInit} from '@angular/core';
import {FILTER, Game, SearchOpt, SEARCHTYPE} from "../../entity";
import {GameService} from "../game.service";

@Component({
    selector: 'app-gamesearch',
    templateUrl: './game-search.component.html',
    styleUrls: ['./game-search.component.css'],
    standalone: false
})
export class GameSearchComponent implements OnInit {

  filter

  searchType = "Simple"

  searchKey: string


  readonly FILTER = FILTER


  includedAdvSearchOpts: SearchOpt[] = []
  excludedAdvSearchOpts: SearchOpt[] = []

  ngOnInit(): void {
  }

  search() {

    // console.log(form);
    // const {searchKey} = form.value;
    //
    // console.log(searchKey)

    const v1 = {"name": {"$regex": `^${this.searchKey}`}}
    const v2 = {"name": {"$regex": `${this.searchKey}`}}

    let filter

    if (this.searchType == "Simple") {
      filter = v1
    } else {
      filter = v2
    }

    this.filter = JSON.stringify(filter)

  }

  swapSearchOpt(searchOpt: SearchOpt, filter: FILTER) {

    if (filter == FILTER.Include) {
      this.removeSearchOpt(searchOpt, FILTER.Include)
      searchOpt.filter = FILTER.Exclude
      this.addSearchOptByFilter(searchOpt, FILTER.Exclude)
    } else {
      this.removeSearchOpt(searchOpt, FILTER.Exclude)
      searchOpt.filter = FILTER.Include
      this.addSearchOptByFilter(searchOpt, FILTER.Include)
    }
  }

  removeSearchOpt(searchOpt: SearchOpt, filter: FILTER) {

    if (filter == FILTER.Include)
      this.includedAdvSearchOpts = this.includedAdvSearchOpts.filter(opt => opt != searchOpt)
    else
      this.excludedAdvSearchOpts = this.excludedAdvSearchOpts.filter(opt => opt != searchOpt)
  }

  addDefaultSearchOpt(filter: FILTER) {

    let searchOpt: SearchOpt = {
      filter: filter,
      key: SEARCHTYPE.TAG,
      value: "",
      enabled: true
    }

    this.addSearchOptByFilter(searchOpt, filter)
  }

  handleSearchClick() {
    // this.page = 1
    this.advSearch()
  }

  constructor(private service: GameService) {
  }

  advSearch() {

    // this.page = 1

    this.load()
    // this.aggregate()
  }

  rowGameList: Game[]

  gamelist: Game[]

  load(){

    let json = this.getAdvSearchJson();

    this.filter=json

    this.service.query(json)
        .subscribe((gs: Game[]) => {

              this.gamelist = gs
              this.rowGameList = gs

              console.log("Gs", gs);

            }
        )
  }

  getAdvSearchJson() {

    let searchOpts = [...this.includedAdvSearchOpts, ...this.excludedAdvSearchOpts]

    let ret: any = {}

    searchOpts.filter(opt => opt.enabled)
        .filter(opt => opt.value?.trim().length > 0)
        .forEach(searchOpt => {

          if (ret.$and == undefined) {
            ret.$and = []
          }

          switch (searchOpt.key) {
            case SEARCHTYPE.REGEX: {
              let raw = {"$regex": searchOpt.value}
              let not = {"$not": raw}
              if (searchOpt.filter == FILTER.Exclude) {
                ret.$and.push({"name": not})
              } else {
                ret.$and.push({"name": raw})
              }

              break
            }
            case SEARCHTYPE.TEXT: {
              let raw = {"$search": searchOpt.value}
              let not = {"$not": raw}
              if (searchOpt.filter == FILTER.Exclude) {
                ret.$and.push({"$text": not})
              } else {
                ret.$and.push({"$text": raw})
              }
              break
            }
            case SEARCHTYPE.TAG: {
              let raw = {"$regex": searchOpt.value.trim()}

              let not = {"$not": raw}
              if (searchOpt.filter == FILTER.Exclude) {
                ret.$and.push({"tags": not})
              } else {
                ret.$and.push({"tags": raw})
              }

              break
            }
          }
        })

    let jsString = JSON.stringify(ret)

    console.log(jsString)

    return jsString;
  }

  addSearchOptByFilter(searchOpt: SearchOpt, filter: FILTER) {

    if (filter == FILTER.Include) {
      this.includedAdvSearchOpts.push(searchOpt)
    } else {
      this.excludedAdvSearchOpts.push(searchOpt)
    }
  }


}
