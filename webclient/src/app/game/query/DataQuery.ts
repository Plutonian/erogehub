import {Brand, Game} from "../../entity";

export class DataQuery {
  rowGameList: Game[]

  filter

  gamelist: Game[]

  onGameDelete(game: Game) {

    console.log(game);
    this.gamelist = this.gamelist.filter(g => g.id != game.id)
    this.rowGameList = this.rowGameList.filter(g => g.id != game.id)
  }

  onBrandSelected(brand: Brand) {

    this.gamelist = this.rowGameList.filter(g => g.brand.id == brand.id)

  }

  onEmotionSelected(emotion: string) {
    this.gamelist = this.rowGameList.filter(g => g.emotion == emotion)
  }

  onLocationSelected(location: string) {
    this.gamelist = this.rowGameList.filter(g => g.location == location)
  }

  onStarSelect(star: number) {
    this.gamelist = this.rowGameList.filter(g => g.star == star)
  }

  onTagSelect(tag: string) {
    this.gamelist = this.rowGameList.filter(g => g.tag.includes(tag))
  }
}
