import {HttpClient} from "@angular/common/http";
import {Injectable} from "@angular/core";
import {APP_SERVER} from "../app.module";


export const GameStates = {
  SAME: {name: "SAME", value: -2},
  BLOCK: {name: "嫌い", value: -99},
  UNCHECKED: {name: "...", value: 0},
  HOPE: {name: "気になり", value: 1},
  PLAYING: {name: "進行中", value: 80},
  PLAYED: {name: "プレイ済み", value: 90}
}

export const GameLocation = {
  REMOTE: {name: "Remote", value: 0},
  LOCAL: {name: "Local", value: 1}
}


@Injectable()
export class GameService {
  constructor(private httpClient: HttpClient) {
  }

  info(id: Number) {
    // @ts-ignore
    return this.httpClient.get(`http://${APP_SERVER}/api/game/detail/${id}`)
  }

  query(filter: string) {
    return this.httpClient.get(`http://${APP_SERVER}/api/game/query?filter=${filter}`)
  }

  delete(id: Number) {
    return this.httpClient.delete(`http://${APP_SERVER}/api/game/${id}`)
  }


  groupByDate(filter: string) {
    return this.httpClient.get(`http://${APP_SERVER}/api/game/groupByDate?filter=${filter}`)
  }

  groupByBrand(filter: string) {
    return this.httpClient.get(`http://${APP_SERVER}/api/game/groupByBrand?filter=${filter}`)
  }

  groupByCV(filter: string) {
    return this.httpClient.get(`http://${APP_SERVER}/api/game/groupByCV?filter=${filter}`)
  }

  groupByTag(filter: string) {
    return this.httpClient.get(`http://${APP_SERVER}/api/game/groupByTag?filter=${filter}`)
  }

  changeState(id: Number, state: Number) {
    return this.httpClient.put(`http://${APP_SERVER}/api/game/${id}/state/${state}`, null)
  }

  changeStar(id: Number, star: Number) {
    return this.httpClient.put(`http://${APP_SERVER}/api/game/${id}/star/${star}`, null)
  }


  changeLocation(id: Number, location: Number) {
    return this.httpClient.put(`http://${APP_SERVER}/api/game/${id}/location/${location}`, null)
  }


}
