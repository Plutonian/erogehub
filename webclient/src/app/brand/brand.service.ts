import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {APP_SERVER} from "../app.module";

export const BrandStates = {
  HATE: {name: "嫌い", value: -99},
  UNCHECKED: {name: "...", value: 0},
  HOPE: {name: "気になり", value: 1},
  LIKE: {name: "好き", value: 99}
}

@Injectable()
export class BrandService {

  constructor(private httpClient: HttpClient) {
  }

  query(filter: string) {
    return this.httpClient.get(`http://${APP_SERVER}/api/brand/query?filter=${filter}`)
  }

  info(id: Number) {
    return this.httpClient.get(`http://${APP_SERVER}/api/brand/${id}`)
  }

  changeState(id: Number, state: Number) {
    return this.httpClient.put(`http://${APP_SERVER}/api/brand/${id}/state/${state}`, null)
  }

}
