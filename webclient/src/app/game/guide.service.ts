import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {APP_SERVER} from "../app.module";

@Injectable()
export class GuideService {

  constructor(private httpClient: HttpClient) {
  }


  search(searchKey: String) {

    console.log(searchKey);
    return this.httpClient.get(`http://${APP_SERVER}/api/game/guide/${searchKey}`)
  }

}
