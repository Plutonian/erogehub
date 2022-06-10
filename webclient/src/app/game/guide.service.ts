import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {environment} from "../../environments/environment";

@Injectable()
export class GuideService {

  constructor(private httpClient: HttpClient) {
  }


  search(searchKey: string) {

    console.log(searchKey);
    return this.httpClient.get(`http://${environment.APP_SERVER}/api/game/guide/${searchKey}`)
  }

}
