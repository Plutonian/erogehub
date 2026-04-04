import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {FILTER, Game, SearchOpt, SEARCHTYPE, Emotion, Location, DateRangeValue, DateRangePresets, getDateRange, Brand, EmotionGroupItem, TagGroupItem, BrandGroupItem, CVGroupItem, StarGroupItem, LocationGroupItem, DateGroupItem, PlayState, PlayStateGroupItem} from "../../entity";
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
  dateRangePresetGroups = [
    { label: '月', options: [
      { label: '这个月', key: 'THIS_MONTH' },
      { label: '过去3个月', key: 'RECENT_3_MONTHS' },
      { label: '过去半年', key: 'RECENT_HALF_YEAR' },
      { label: '未来3个月', key: 'NEXT_3_MONTHS' },
      { label: '未来半年', key: 'NEXT_HALF_YEAR' }
    ]},
    { label: '年', options: [
      { label: '过去3年', key: 'RECENT_3_YEARS' },
      { label: '过去5年', key: 'RECENT_5_YEARS' },
      { label: '过去10年', key: 'RECENT_10_YEARS' }
    ]}
  ]
  customPreset = { label: '自定义', key: 'CUSTOM' }
  selectedDatePreset = 'THIS_MONTH'
  customDateRange: DateRangeValue = { startDate: '', endDate: '' }
  currentDateRange: DateRangeValue = { startDate: '', endDate: '' }
  dateRangeEnabled: boolean = true

  // 聚合数据
  emotionGroup: EmotionGroupItem[]
  tagGroup: TagGroupItem[]
  brandGroup: BrandGroupItem[]
  cvGroup: CVGroupItem[]
  trueCVGroup: CVGroupItem[]
  starGroup: StarGroupItem[]
  locationGroup: LocationGroupItem[]
  playStateGroup: PlayStateGroupItem[]
  dateGroup: DateGroupItem[]

  constructor(private service: GameService, private router: Router) {
  }

  ngOnInit(): void {
    // 初始化日期范围为"这个月"
    const range = getDateRange('THIS_MONTH')
    this.currentDateRange = range
    this.customDateRange = { ...range }
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
      value: emotion,
      enabled: true,
      filter: FILTER.Include
    })
  }

  onLocationSelected(location: Location) {
    this.includedAdvSearchOpts.push({
      key: SEARCHTYPE.LOCATION,
      value: location,
      enabled: true,
      filter: FILTER.Include
    })
  }

  onPlayStateSelected(playState: PlayState) {
    this.includedAdvSearchOpts.push({
      key: SEARCHTYPE.PLAYSTATE,
      value: playState,
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

  onTrueCVSelect(cv: string) {
    this.includedAdvSearchOpts.push({
      key: SEARCHTYPE.TRUECV,
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

    this.service.groupByTrueCV(json)
      .subscribe((data: CVGroupItem[]) => this.trueCVGroup = data)

    this.service.groupByStar(json)
      .subscribe((data: StarGroupItem[]) => this.starGroup = data)

    this.service.groupByLocation(json)
      .subscribe((data: LocationGroupItem[]) => this.locationGroup = data)

    this.service.groupByPlayState(json)
      .subscribe((data: PlayStateGroupItem[]) => this.playStateGroup = data)

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

          gs.forEach(g=> {
            g._publishDate = new Date(g.publishDate)
            g._emotion = g.emotion !== undefined ? g.emotion as Emotion : Emotion.NORMAL
            g._location = g.location !== undefined ? g.location as Location : Location.REMOTE
            g._playState = g.playState !== undefined ? g.playState as PlayState : PlayState.NOT_PLAY
          })
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

          const isExclude = searchOpt.filter == FILTER.Exclude

          switch (searchOpt.key) {
            case SEARCHTYPE.REGEX: {
              let raw = {"$regex": searchOpt.value}
              ret.$and.push({"name": isExclude ? {"$not": raw} : raw})
              break
            }
            case SEARCHTYPE.TEXT: {
              let raw = {"$search": searchOpt.value}
              ret.$and.push({"$text": isExclude ? {"$not": raw} : raw})
              break
            }
            case SEARCHTYPE.TAG: {
              let raw = {"$regex": String(searchOpt.value).trim()}
              ret.$and.push({"tag": isExclude ? {"$not": raw} : raw})
              break
            }
            case SEARCHTYPE.STAR: {
              let starValue = parseInt(String(searchOpt.value))
              if (!isNaN(starValue)) {
                ret.$and.push({"star": isExclude ? {"$ne": starValue} : starValue})
              }
              break
            }
            case SEARCHTYPE.EMOTION: {
              const emotionVal = Number(searchOpt.value)
              if (!isNaN(emotionVal)) {
                ret.$and.push({"emotion": isExclude ? {"$ne": emotionVal} : emotionVal})
              }
              break
            }
            case SEARCHTYPE.LOCATION: {
              const locationVal = Number(searchOpt.value)

              console.log(locationVal);
              if (!isNaN(locationVal)) {
                ret.$and.push({"location": isExclude ? {"$ne": locationVal} : locationVal})
              }
              break
            }
            case SEARCHTYPE.PLAYSTATE: {
              const playStateVal = Number(searchOpt.value)
              if (!isNaN(playStateVal)) {
                ret.$and.push({"playState": isExclude ? {"$ne": playStateVal} : playStateVal})
              }
              break
            }
            case SEARCHTYPE.PAINTER: {
              const painterVal = String(searchOpt.value)
              ret.$and.push({"painter": isExclude ? {"$ne": painterVal} : painterVal})
              break
            }
            case SEARCHTYPE.WRITER: {
              const writerVal = String(searchOpt.value)
              ret.$and.push({"writer": isExclude ? {"$ne": writerVal} : writerVal})
              break
            }
            case SEARCHTYPE.CV: {
              const cvVal = {"$regex": String(searchOpt.value)}
              ret.$and.push({"gamechar.cv": isExclude ? {"$not": cvVal} : cvVal})
              break
            }
            case SEARCHTYPE.TRUECV: {
              const truecvVal = {"$regex": String(searchOpt.value)}
              ret.$and.push({"gamechar.truecv": isExclude ? {"$not": truecvVal} : truecvVal})
              break
            }
            case SEARCHTYPE.BRAND: {
              const brandVal = {"$regex": String(searchOpt.value)}
              ret.$and.push({"brand.name": isExclude ? {"$not": brandVal} : brandVal})
              break
            }
          }
        })

    // 添加日期范围查询
    if (this.dateRangeEnabled && (this.currentDateRange.startDate || this.currentDateRange.endDate)) {
      if (ret.$and == undefined) {
        ret.$and = []
      }

      const dateQuery: any = {}

      if (this.currentDateRange.startDate) {
        // startDate: 当月1日 00:00:00
        const [year, month] = this.currentDateRange.startDate.split('-').map(Number)
        const start = new Date(year, month - 1, 1).getTime()
        dateQuery.$gte = {"$date": start}
      }
      if (this.currentDateRange.endDate) {
        // endDate: 下个月1日 00:00:00 (范围 [start, end))
        const [year, month] = this.currentDateRange.endDate.split('-').map(Number)
        const end = new Date(year, month, 1).getTime()
        dateQuery.$lt = {"$date": end}
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
      // 使用自定义日期范围，如果已有值则保留
      this.currentDateRange = {
        startDate: this.customDateRange.startDate,
        endDate: this.customDateRange.endDate
      }
    } else {
      // 使用预设值，同时更新自定义日期范围
      const range = getDateRange(this.selectedDatePreset)
      this.currentDateRange = range
      this.customDateRange = { ...range }
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

  adjustDateRange(delta: number, type: 'year' | 'month') {
    // 切换到自定义模式
    this.selectedDatePreset = 'CUSTOM'

    // 解析当前日期范围 (YYYY-MM 格式)
    const parseMonth = (str: string) => {
      if (!str) return new Date()
      const [year, month] = str.split('-').map(Number)
      return new Date(year, month - 1, 1)
    }
    const start = parseMonth(this.currentDateRange.startDate)
    const end = parseMonth(this.currentDateRange.endDate)

    // 调整日期
    if (type === 'year') {
      start.setFullYear(start.getFullYear() + delta)
      end.setFullYear(end.getFullYear() + delta)
    } else {
      start.setMonth(start.getMonth() + delta)
      end.setMonth(end.getMonth() + delta)
    }

    // 格式化日期 (YYYY-MM)
    const formatMonth = (date: Date) => {
      const year = date.getFullYear()
      const month = String(date.getMonth() + 1).padStart(2, '0')
      return `${year}-${month}`
    }

    // 更新日期范围
    const newRange = {
      startDate: formatMonth(start),
      endDate: formatMonth(end)
    }
    this.currentDateRange = newRange
    this.customDateRange = { ...newRange }
  }


}
