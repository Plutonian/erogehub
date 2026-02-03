import {Pipe} from "@angular/core";
import {Game, Location, GamePlayStates, Emotion, PlayState} from "./entity";
import {environment} from "../environments/environment";

@Pipe({
    name: "img",
    standalone: false
})
export class ImgUrlPipl {

  transform(g: Game) {
     let date= g._publishDate

    return `http://${environment.IMG_SERVER}/img/${date.getFullYear()}/${date.getMonth()+1}/${g?.id}`
  }

}


@Pipe({
    name: "wiki",
    standalone: false
})
export class WikiPipl {

  transform(name: string) {
    return `https://ja.wikipedia.org/w/index.php?search=${name}`
  }

}

@Pipe({
    name: "bangumi",
    standalone: false
})
export class BangumiPipl {

  transform(name: string) {
    return `http://bgm.tv/subject_search/${name}`
  }

}


@Pipe({
    name: "playStateJP",
    standalone: false
})
export class PlayStatePipl {

  transform(state: string) {
    return GamePlayStates[`${state}`].name
  }

}

const EmotionNames: Record<number, string> = {
  [Emotion.HATE]: "嫌い",
  [Emotion.NORMAL]: "...",
  [Emotion.HOPE]: "気になり",
  [Emotion.LIKE]: "好き"
}

@Pipe({
    name: "emotionJP",
    standalone: false
})
export class EmotionJPPipe {

  transform(emotion: Emotion | number | string): string {
    const value = Number(emotion)
    return EmotionNames[value] || String(emotion)
  }

}

const LocationNames: Record<number, string> = {
  [Location.REMOTE]: "Remote",
  [Location.LOCAL]: "Local"
}

@Pipe({
    name: "loc",
    standalone: false
})
export class LocationPipl {

  transform(location: Location | number | string): string {
    const value = Number(location)
    return LocationNames[value] || String(location)
  }

}

const PlayStateNames: Record<number, string> = {
  [PlayState.NOT_PLAY]: "...",
  [PlayState.PLAYING]: "進行中",
  [PlayState.PLAYED]: "プレイ済み"
}

@Pipe({
    name: "playState",
    standalone: false
})
export class PlayStatePipe {

  transform(state: PlayState | number | string): string {
    const value = Number(state)
    return PlayStateNames[value] || String(state)
  }

}

@Pipe({
    name: "block",
    standalone: false
})
export class BlockPipl {

  transform(state: string) {

    // switch (state) {
    //   case "BLOCK":"block"
    //
    // }
    return state == "BLOCK" ? "block" : ""
  }

}


