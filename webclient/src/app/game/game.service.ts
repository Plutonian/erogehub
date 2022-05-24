import {HttpClient} from "@angular/common/http";
import {Injectable} from "@angular/core";

@Injectable()
export class GameService {
  constructor(private httpClient: HttpClient) {
  }

  info(id: Number) {
    // @ts-ignore
    return this.httpClient.get(`http://192.168.2.214:9000/api/game/detail/${id}`)
  }
}
