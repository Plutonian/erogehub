import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {environment} from "../environments/environment";

@Injectable()
export class AppService {

  constructor(private httpClient: HttpClient) {
  }

  emotions() {
    return this.httpClient.get(`http://${environment.APP_SERVER}/api/app/emotions`)
  }


}
