export interface Game {
  id: number;
  name: string;
  painter: string[];
  tag: string[];
  publishDate: MyDate
  dateString: string
  dateFormatString: string
  published: Boolean
  brand: Brand
  story: string;
  intro: string
  state: string
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
  "state": string
  javaTag: string[]
}

export interface Guide {
  title: string,
  href: string,
  from: string
}


export interface CV {
  name: string
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
