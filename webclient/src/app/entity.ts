export interface Game {
  id: number
  name: string
  painter: string[]
  writer: string[]
  tag: string[]
  publishDate: MyDate
  dateString: string
  dateFormatString: string
  published: Boolean
  brand: Brand
  story: string
  intro: string
  emotion: string
  playState: string
  location: string
  star: number
  isSame: boolean
  titles: Titles
  gameImgs: GameImg[]
  gameCharacters: GameCharacter[]
}

export interface Emotion {
  name: string
  value: number
}


export interface Titles {
  mainTitle: string
  subTitle: string
}

export interface GameImg {
  index: number
}

export interface GameCharacter {
  "name": string
  "cv": string
  "intro": string
  "trueCV": string
  "index": number
  "img": string
  "heroine": boolean
  "cvObj"?: CV
}

export interface MyDate {
  year: number
  monthValue: number
  dayOfMonth: number
}


export interface Brand {
  id: number
  name: string
  "website": string
  "comp": string
  "emotion": string
  javaTag: string[],
  series?: Series[]
}

export interface Series {
  name: string,
  games: Game[]
}

export interface Guide {
  title: string,
  href: string,
  from: string
}


export interface CV {
  name: string,
  trueName: string,
  star: number,
  tag: string[],
  statistics: GameStatistics
}

export interface GameStatistics {

  start: MyDate,
  end: MyDate,
  count: number,
  realCount: number,
  emotion: EmotionStatistics,
  star: StarStatistics,
  location: LocationStatistics

}

export interface EmotionStatistics {
  like: number,
  hope: number,
  normal: number,
  hate: number
}

export interface StarStatistics {
  zero: number,
  one: number,
  two: number,
  three: number,
  four: number,
  five: number
}

export interface LocationStatistics {
  local: number,
  remote: number
}

export interface TagGroup {
  name: string
  javaTags: string[]
}

export interface CVGroup {
  star: number
  list: CV[]
}

export interface GroupItem {
  title: string
  count: number
}

export interface TagGroupItem extends GroupItem {
}


export interface EmotionGroupItem extends GroupItem {
  emotion: string
}

export interface StarGroupItem extends GroupItem {
  star: number
}

export interface CVGroupItem extends GroupItem {
  real: Boolean
}

interface CompositeGroupItem extends GroupItem {
  children?: CompositeGroupItem[]
  games?: Game[]
}

export interface DateRange {
  start: MyDate
  end: MyDate
}

export interface DateGroupItem extends CompositeGroupItem {
  index: number
  range: DateRange,
  dateType: string,
}

export interface BrandGroupItem extends CompositeGroupItem {
  comp?: string,
  brand?: Brand
}

export interface State {
  name: string
  value: number
}
