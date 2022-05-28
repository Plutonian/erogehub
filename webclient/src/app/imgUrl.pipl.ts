import {Pipe} from "@angular/core";
import {Game} from "./entity";
import {IMG_SERVER} from "./app.module";
import {GameLocation, GameStates} from "./game/game.service";

@Pipe({name: "img"})
export class ImgUrlPipl {

  transform(g: Game) {
    return `http://${IMG_SERVER}/game/${g?.publishDate?.year}/${g?.publishDate?.monthValue}/${g?.id}`
  }

}

@Pipe({name: "wiki"})
export class WikiPipl {

  transform(name: String) {
    return `https://ja.wikipedia.org/w/index.php?search=${name}`
  }

}

@Pipe({name: "bangumi"})
export class BangumiPipl {

  transform(name: String) {
    return `http://bgm.tv/subject_search/${name}`
  }

}

@Pipe({name: "jp"})
export class StatePipl {

  transform(state: String) {
    return GameStates[`${state}`].name
  }

}

@Pipe({name: "loc"})
export class LocationPipl {

  transform(state: String) {
    return GameLocation[`${state}`].name
  }

}
