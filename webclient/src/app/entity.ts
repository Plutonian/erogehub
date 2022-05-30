export interface Game {
  id: Number;
  name: String;
  painter: String[];
  tag: String[];
  publishDate: MyDate
  dateString: String
  dateFormatString: String
  published: Boolean
  brand: Brand
  story: String;
  intro: String
  state: String
  location: String
  star: Number
  titles: Titles
  gameImgs: GameImg[]
  gameCharacters: GameCharacter[]
}


export interface Titles {
  mainTitle: String
  subTitle: String
}

export interface GameImg {
  index: Number
}

export interface GameCharacter {
  "name": String
  "cv": String
  "intro": String
  "trueCV": String
  "index": Number
}

export interface MyDate {
  year: Number
  monthValue: Number
  dayOfMonth: Number
}


export interface Brand {
  id: Number
  name: String
  "website": String
  "comp": String
  "state": String
  javaTag: String[]
}

export interface Guide {
  title: String,
  href: String,
  from: String
}


export interface CV {
  name: String
}

export interface TagGroup {
  name: String
  javaTags: String[]
}

export interface CVGroup {
  star: Number
  list: CV[]
}

export interface GroupItem {
  title: String
  count: Number
}

export interface TagGroupItem extends GroupItem {
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
  range: DateRange,
  dateType: String,
}

export interface BrandGroupItem extends CompositeGroupItem {
  comp?: String,
  brand?: Brand
}

export interface State {
  name: String
  value: Number
}
