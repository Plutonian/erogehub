import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {FILTER, Game, SearchOpt, SEARCHTYPE, Emotion, GamePlayStates, GameLocation, DateRangePreset, DateRangeValue, DateRangePresets, getDateRange, Brand, EmotionGroupItem, TagGroupItem, BrandGroupItem, CVGroupItem, StarGroupItem, LocationGroupItem, DateGroupItem} from "../../entity";
import {GameService} from "../game.service";
import {Router} from "@angular/router";

@Component({
    selector: 'app-gamesearch',
    templateUrl: './game-search.component.html',
    styleUrls: ['./game-search.component.css'],
    standalone: false
})
export class GameSearchComponent implements OnInit {


  page: number = 1

  total: number = 0

  pageItemSize: number = 40

  @Output()
  pageChange = new EventEmitter<number>


  readonly FILTER = FILTER


  includedAdvSearchOpts: SearchOpt[] = []
  excludedAdvSearchOpts: SearchOpt[] = []

  // 日期范围相关
  dateRangePresets = DateRangePresets
  selectedDatePreset = 'ALL'
  customDateRange: DateRangeValue = { startDate: '', endDate: '' }
  currentDateRange: DateRangeValue = { startDate: '', endDate: '' }

  // 聚合数据
  emotionGroup: EmotionGroupItem[]
  tagGroup: TagGroupItem[]
  brandGroup: BrandGroupItem[]
  cvGroup: CVGroupItem[]
  starGroup: StarGroupItem[]
  locationGroup: LocationGroupItem[]
  dateGroup: DateGroupItem[]

  constructor(private service: GameService, private router: Router) {
  }

  ngOnInit(): void {
  }

  // 聚合面板事件处理
  onBrandSelected(brand: Brand) {
    this.includedAdvSearchOpts.push({
      key: SEARCHTYPE.BRAND,
      value: brand.name,
      enabled: true,
      filter: FILTER.Include
    })
  }

  onTagSelect(tag: string) {
    // 添加标签搜索条件
    this.includedAdvSearchOpts.push({
      key: SEARCHTYPE.TAG,
      value: tag,
      enabled: true,
      filter: FILTER.Include
    })
  }

  onEmotionSelected(emotion: string | Emotion) {
    this.includedAdvSearchOpts.push({
      key: SEARCHTYPE.EMOTION,
      value: String(emotion),
      enabled: true,
      filter: FILTER.Include
    })
  }

  onLocationSelected(location: string) {
    this.includedAdvSearchOpts.push({
      key: SEARCHTYPE.LOCATION,
      value: location,
      enabled: true,
      filter: FILTER.Include
    })
  }

  onCVSelect(cv: string) {
    this.includedAdvSearchOpts.push({
      key: SEARCHTYPE.CV,
      value: cv,
      enabled: true,
      filter: FILTER.Include
    })
  }

  onStarSelect(star: number) {
    this.includedAdvSearchOpts.push({
      key: SEARCHTYPE.STAR,
      value: String(star),
      enabled: true,
      filter: FILTER.Include
    })
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
    this.page = 1
    this.advSearch()
  }

  advSearch() {

    this.page = 1

    this.load()
    this.loadAggregateData()
  }

  loadAggregateData() {
    const json = this.getAdvSearchJson()

    this.service.groupByEmotion(json)
      .subscribe((data: EmotionGroupItem[]) => this.emotionGroup = data)

    this.service.groupByTag(json)
      .subscribe((data: TagGroupItem[]) => this.tagGroup = data)

    this.service.groupByBrand(json)
      .subscribe((data: BrandGroupItem[]) => this.brandGroup = data)

    this.service.groupByCV(json)
      .subscribe((data: CVGroupItem[]) => this.cvGroup = data)

    this.service.groupByStar(json)
      .subscribe((data: StarGroupItem[]) => this.starGroup = data)

    this.service.groupByLocation(json)
      .subscribe((data: LocationGroupItem[]) => this.locationGroup = data)

    this.service.groupByDate(json)
      .subscribe((data: DateGroupItem[]) => this.dateGroup = data)
  }

  gamelist: Game[]

  load(){

    this.pageItemSize=40


    let json = this.getAdvSearchJson();


    this.service.count(json).subscribe((total: number) => {
      this.total = total
    })

    this.service.queryData(json, this.page, this.pageItemSize)
        .subscribe((gs: Game[]) => {

          gs.forEach(g=>g._publishDate=new Date(g.publishDate))
          this.gamelist = gs

          console.log("Gs", gs);

        })
  }

  onPageIndexChange(page: number) {

    this.page = page

    this.load()


  }

  getAdvSearchJson() {

    let searchOpts = [...this.includedAdvSearchOpts, ...this.excludedAdvSearchOpts]

    let ret: any = {}

    searchOpts.filter(opt => opt.enabled)
        .filter(opt => opt.value != null && String(opt.value).trim().length > 0)
        .forEach(searchOpt => {

          if (ret.$and == undefined) {
            ret.$and = []
          }

          const not = (val) => searchOpt.filter == FILTER.Exclude ? {"$not": val} : val

          switch (searchOpt.key) {
            case SEARCHTYPE.REGEX: {
              let raw = {"$regex": searchOpt.value}
              if (searchOpt.filter == FILTER.Exclude) {
                ret.$and.push({"name": not(raw)})
              } else {
                ret.$and.push({"name": raw})
              }
              break
            }
            case SEARCHTYPE.TEXT: {
              let raw = {"$search": searchOpt.value}
              if (searchOpt.filter == FILTER.Exclude) {
                ret.$and.push({"$text": not(raw)})
              } else {
                ret.$and.push({"$text": raw})
              }
              break
            }
            case SEARCHTYPE.TAG: {
              let raw = {"$regex": String(searchOpt.value).trim()}
              ret.$and.push({"tag": not(raw)})
              break
            }
            case SEARCHTYPE.STAR: {
              let starValue = parseInt(String(searchOpt.value))
              if (!isNaN(starValue)) {
                ret.$and.push({"star": not(starValue)})
              }
              break
            }
            case SEARCHTYPE.EMOTION: {
              const emotionVal = Number(searchOpt.value)
              if (!isNaN(emotionVal)) {
                ret.$and.push({"emotion": not(emotionVal)})
              }
              break
            }
            case SEARCHTYPE.LOCATION: {
              const locationKey = String(searchOpt.value)
              const locationVal = GameLocation[locationKey]?.value
              if (locationVal !== undefined) {
                ret.$and.push({"location": not(locationVal)})
              }
              break
            }
            case SEARCHTYPE.PLAYSTATE: {
              const playStateKey = String(searchOpt.value)
              const playStateVal = GamePlayStates[playStateKey]?.value
              if (playStateVal !== undefined) {
                ret.$and.push({"playState": not(playStateVal)})
              }
              break
            }
            case SEARCHTYPE.PAINTER: {
              ret.$and.push({"painter": not(String(searchOpt.value))})
              break
            }
            case SEARCHTYPE.WRITER: {
              ret.$and.push({"writer": not(String(searchOpt.value))})
              break
            }
            case SEARCHTYPE.CV: {
              ret.$and.push({"gamechar.cv": not({"$regex": String(searchOpt.value)})})
              break
            }
            case SEARCHTYPE.BRAND: {
              ret.$and.push({"brand.name": not({"$regex": String(searchOpt.value)})})
              break
            }
          }
        })

    // 添加日期范围查询
    if (this.selectedDatePreset !== 'ALL' && (this.currentDateRange.startDate || this.currentDateRange.endDate)) {
      if (ret.$and == undefined) {
        ret.$and = []
      }

      const dateQuery: any = {}

      if (this.currentDateRange.startDate) {
        const start = Date.parse(`${this.currentDateRange.startDate} 00:00:00`)
        dateQuery.$gte = {"$date": start}
      }
      if (this.currentDateRange.endDate) {
        const end = Date.parse(`${this.currentDateRange.endDate} 23:59:59`)
        dateQuery.$lte = {"$date": end}
      }

      ret.$and.push({"publishDate": dateQuery})
    }

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

  // 日期范围相关方法
  onDatePresetChange() {
    if (this.selectedDatePreset === 'CUSTOM') {
      // 清空当前范围，等待用户手动输入
      this.currentDateRange = { startDate: '', endDate: '' }
    } else if (this.selectedDatePreset === 'ALL') {
      this.currentDateRange = { startDate: '', endDate: '' }
    } else {
      // 使用预设值
      const range = getDateRange(this.selectedDatePreset)
      this.currentDateRange = range
    }
  }

  onCustomDateChange() {
    if (this.customDateRange.startDate || this.customDateRange.endDate) {
      this.currentDateRange = {
        startDate: this.customDateRange.startDate,
        endDate: this.customDateRange.endDate
      }
    } else {
      this.currentDateRange = { startDate: '', endDate: '' }
    }
  }


}
