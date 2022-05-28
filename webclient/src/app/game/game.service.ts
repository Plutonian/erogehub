import {HttpClient} from "@angular/common/http";
import {Injectable} from "@angular/core";
import {APP_SERVER} from "../app.module";


export const GameStates = {
  PACKAGE: {name: "ポケ版", value: -3},
  SAME: {name: "SAME", value: -2},
  BLOCK: {name: "ブロック", value: -1},
  UNCHECKED: {name: "-", value: 0},
  HOPE: {name: "気になり", value: 3},
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

  query(filter: String) {
    return this.httpClient.get(`http://${APP_SERVER}/api/query?filter=${filter}`)
  }


  groupByDate(filter: String) {
    return this.httpClient.get(`http://${APP_SERVER}/api/game/groupByDate?filter=${filter}`)
  }

  groupByCV(filter: String) {
    return this.httpClient.get(`http://${APP_SERVER}/api/game/groupByCV?filter=${filter}`)
  }

  groupByTag(filter: String) {
    return this.httpClient.get(`http://${APP_SERVER}/api/game/groupByTag?filter=${filter}`)
  }

  changeState(id: Number, state: Number) {
    return this.httpClient.put(`http://${APP_SERVER}/api/game/${id}/state/${state}`, null)
  }

  changeLocation(id: Number, location: Number) {
    return this.httpClient.put(`http://${APP_SERVER}/api/game/${id}/location/${location}`, null)
  }


}
