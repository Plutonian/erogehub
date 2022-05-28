export interface Game {
  id: Number;
  name: String;
  painter: String[];
  tag: String[];
  publishDate: Date
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

export interface Date {
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

export interface CVGroupItem {
  title: String
  count: Number
  real: Boolean
}

export interface DateGroupItem {
  title: String,
  // range: DateRange,
  count: Number,
  dateType: String,
  children: DateGroupItem[]
}

export interface State {
  name: String
  value: Number
}
