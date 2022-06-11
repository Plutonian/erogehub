import {Pipe} from "@angular/core";
import {Game, MyDate} from "./entity";
import {GameLocation, GamePlayStates} from "./game/game.service";
import {Emotions} from "./brand/brand.service";
import {environment} from "../environments/environment";

@Pipe({name: "img"})
export class ImgUrlPipl {

  transform(g: Game) {
    return `http://${environment.IMG_SERVER}/game/${g?.publishDate?.year}/${g?.publishDate?.monthValue}/${g?.id}`
  }

}

@Pipe({name: "dateStr"})
export class MyDatePipl {

  transform(date: MyDate) {
    return `${date.year}-${date.monthValue}-${date.dayOfMonth}`
  }

}


@Pipe({name: "wiki"})
export class WikiPipl {

  transform(name: string) {
    return `https://ja.wikipedia.org/w/index.php?search=${name}`
  }

}

@Pipe({name: "bangumi"})
export class BangumiPipl {

  transform(name: string) {
    return `http://bgm.tv/subject_search/${name}`
  }

}


@Pipe({name: "playStateJP"})
export class PlayStatePipl {

  transform(state: string) {
    return GamePlayStates[`${state}`].name
  }

}

@Pipe({name: "emotionJP"})
export class EmotionJPPipe {

  transform(emotion: string) {
    return Emotions[`${emotion}`].name
  }

}

@Pipe({name: "loc"})
export class LocationPipl {

  transform(state: string) {
    return GameLocation[`${state}`].name
  }

}

@Pipe({name: "block"})
export class BlockPipl {

  transform(state: string) {

    // switch (state) {
    //   case "BLOCK":"block"
    //
    // }
    return state == "BLOCK" ? "block" : ""
  }

}


