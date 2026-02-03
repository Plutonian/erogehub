export interface Game {
  id: number
  name: string
  painter: string[]
  writer: string[]
  tag: string[]
  _publishDate: Date
  publishDate: number
  brand: Brand
  story: string
  intro: string
  emotion: number
  _emotion: Emotion
  playState:number
  _playState: PlayState
  location: number
  _location: Location
  star: number
  isSame: boolean
  mainTitle: string
  subTitle: string
  simpleImg: GameImg[]
  gamechar: GameCharacter[]
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

  start: string,
  end: string,
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
  emotion: number
}

export interface LocationGroupItem extends GroupItem {
  location: number
}

export interface StarGroupItem extends GroupItem {
  star: number
}

export interface PlayStateGroupItem extends GroupItem {
  playState: number
}

export interface CVGroupItem extends GroupItem {
  real: Boolean
}

interface CompositeGroupItem extends GroupItem {
  children?: CompositeGroupItem[]
  games?: Game[]
}

export interface DateRange {
  start: string
  end: string
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

export enum SEARCHTYPE {
  TEXT,
  REGEX,
  TAG,
  STAR,
  EMOTION,
  LOCATION,
  PLAYSTATE,
  PAINTER,
  WRITER,
  CV,
  TRUECV,
  BRAND
}

export enum FILTER {
  Include,
  Exclude
}

export interface SearchOpt {
  key: SEARCHTYPE,
  value: string|number|Emotion|Location|PlayState,
  enabled: boolean,
  filter: FILTER
}

export enum Emotion {
  HATE = -99,
  NORMAL = 0,
  HOPE = 1,
  LIKE = 99
}

export enum Location {
  REMOTE = 0,
  LOCAL = 1
}

export enum PlayState {
  NOT_PLAY = 0,
  PLAYING = 1,
  PLAYED = 2
}

export const GamePlayStates = {
  NOT_PLAY: {name: "...", value: 0},
  PLAYING: {name: "進行中", value: 1},
  PLAYED: {name: "プレイ済み", value: 2}
}

export interface DateRangeValue {
  startDate: string  // 格式: YYYY-MM-DD
  endDate: string    // 格式: YYYY-MM-DD
}

export interface DateRangePreset {
  label: string
  key: string
}

export const DateRangePresets: DateRangePreset[] = [
  { label: "全部", key: "ALL" },
  { label: "今月", key: "THIS_MONTH" },
  { label: "今年", key: "THIS_YEAR" },
  { label: "最近3个月", key: "RECENT_3_MONTHS" },
  { label: "最近半年", key: "RECENT_HALF_YEAR" },
  { label: "去年", key: "LAST_YEAR" },
  { label: "上一个月", key: "LAST_MONTH" },
  { label: "自定义", key: "CUSTOM" }
]

export function getDateRange(presetKey: string): { startDate: string, endDate: string } {
  const now = new Date()
  const formatDate = (date: Date) => {
    const year = date.getFullYear()
    const month = String(date.getMonth() + 1).padStart(2, '0')
    const day = String(date.getDate()).padStart(2, '0')
    return `${year}-${month}-${day}`
  }

  switch (presetKey) {
    case 'THIS_MONTH': {
      const start = new Date(now.getFullYear(), now.getMonth(), 1)
      const end = new Date(now.getFullYear(), now.getMonth() + 1, 0)
      return { startDate: formatDate(start), endDate: formatDate(end) }
    }
    case 'THIS_YEAR': {
      const start = new Date(now.getFullYear(), 0, 1)
      const end = new Date(now.getFullYear(), 11, 31)
      return { startDate: formatDate(start), endDate: formatDate(end) }
    }
    case 'RECENT_3_MONTHS': {
      const start = new Date(now.getFullYear(), now.getMonth() - 2, 1)
      const end = new Date()
      return { startDate: formatDate(start), endDate: formatDate(end) }
    }
    case 'RECENT_HALF_YEAR': {
      const start = new Date(now.getFullYear(), now.getMonth() - 5, 1)
      const end = new Date()
      return { startDate: formatDate(start), endDate: formatDate(end) }
    }
    case 'LAST_YEAR': {
      const lastYear = now.getFullYear() - 1
      const start = new Date(lastYear, 0, 1)
      const end = new Date(lastYear, 11, 31)
      return { startDate: formatDate(start), endDate: formatDate(end) }
    }
    case 'LAST_MONTH': {
      const start = new Date(now.getFullYear(), now.getMonth() - 1, 1)
      const end = new Date(now.getFullYear(), now.getMonth(), 0)
      return { startDate: formatDate(start), endDate: formatDate(end) }
    }
    case 'ALL':
    case 'CUSTOM':
    default:
      return { startDate: '', endDate: '' }
  }
}
