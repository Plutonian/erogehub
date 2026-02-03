import {Pipe} from "@angular/core";
import {Game, MyDate, GameLocation, GamePlayStates, Emotions} from "./entity";
import {environment} from "../environments/environment";

@Pipe({
    name: "img",
    standalone: false
})
export class ImgUrlPipl {

  transform(g: Game) {
    return `http://${environment.IMG_SERVER}/img/${g?.publishDate?.year}/${g?.publishDate?.monthValue}/${g?.id}`
  }

}

@Pipe({
    name: "dateStr",
    standalone: false
})
export class MyDatePipl {

  transform(date: MyDate) {
    return `${date.year}-${date.monthValue}-${date.dayOfMonth}`
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

@Pipe({
    name: "emotionJP",
    standalone: false
})
export class EmotionJPPipe {

  transform(emotion: string) {
    return Emotions[`${emotion}`].name
  }

}

@Pipe({
    name: "loc",
    standalone: false
})
export class LocationPipl {

  transform(state: string) {
    return GameLocation[`${state}`].name
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


