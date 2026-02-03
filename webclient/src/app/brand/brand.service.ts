import {Injectable} from "@angular/core";
import { HttpClient } from "@angular/common/http";
import {environment} from "../../environments/environment";

@Injectable()
export class BrandService {

  constructor(private httpClient: HttpClient) {
  }

  query(filter: string) {
    return this.httpClient.get(`http://${environment.APP_SERVER}/api/brand/query?filter=${filter}`)
  }

  info(id: Number) {
    return this.httpClient.get(`http://${environment.APP_SERVER}/api/brand/${id}`)
  }

  changeState(id: Number, state: Number) {
    return this.httpClient.put(`http://${environment.APP_SERVER}/api/brand/${id}/state/${state}`, null)
  }

}
