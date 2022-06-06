import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {APP_SERVER} from "./app.module";

@Injectable()
export class AppService {

  constructor(private httpClient: HttpClient) {
  }

  emotions() {
    return this.httpClient.get(`http://${APP_SERVER}/api/app/emotions`)
  }


}
