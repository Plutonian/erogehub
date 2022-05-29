import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {APP_SERVER} from "../app.module";

export const BrandStates = {
  ALL: {name: "ALL", value: -99},
  BLOCK: {name: "Ignore", value: -2},
  UNCHECKED: {name: "...", value: 0},
  HOPE: {name: "気になり", value: 80},
  LIKE: {name: "好き", value: 99},
  MARK: {name: "後で見る", value: 100}
}

@Injectable()
export class BrandService {

  constructor(private httpClient: HttpClient) {
  }

  query(filter: String) {
    return this.httpClient.get(`http://${APP_SERVER}/api/brand/query?filter=${filter}`)
  }

  info(id: Number) {
    return this.httpClient.get(`http://${APP_SERVER}/api/brand/${id}`)
  }

  changeState(id: Number, state: Number) {
    return this.httpClient.put(`http://${APP_SERVER}/api/brand/${id}/state/${state}`, null)
  }

}
