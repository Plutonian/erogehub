import { HttpClient } from "@angular/common/http";
import {Injectable} from "@angular/core";
import {environment} from "../../environments/environment";


@Injectable()
export class GameService {
  constructor(private httpClient: HttpClient) {
  }

  info(id: number) {
    // @ts-ignore
    return this.httpClient.get(`http://${environment.APP_SERVER}/api/game/detail/${id}`)
  }

  count(filter: string) {
    return this.httpClient.post(`http://${environment.APP_SERVER}/api/game/count`,
        {
          "filter": filter,
        }
    )
  }

  queryData(filter: string, page: number, pageItemSize: number) {
    return this.httpClient.post(`http://${environment.APP_SERVER}/api/game/queryData`,
        {
          "filter": filter,
          "page": page,
          "pageItemSize": pageItemSize,
        }
    )
  }

  delete(id: number) {
    return this.httpClient.delete(`http://${environment.APP_SERVER}/api/game/${id}`)
  }

  updateTags(id: number, tags: string[]) {
    return this.httpClient.put(`http://${environment.APP_SERVER}/api/game/${id}/updateTags`, {
      "tags": tags
    })
  }


  groupByDate(filter: string) {
    return this.httpClient.get(`http://${environment.APP_SERVER}/api/game/groupByDate?filter=${filter}`)
  }

  groupByBrand(filter: string) {
    return this.httpClient.get(`http://${environment.APP_SERVER}/api/game/groupByBrand?filter=${filter}`)
  }

  groupByCV(filter: string) {
    return this.httpClient.get(`http://${environment.APP_SERVER}/api/game/groupByCV?filter=${filter}`)
  }

  groupByTag(filter: string) {
    return this.httpClient.get(`http://${environment.APP_SERVER}/api/game/groupByTag?filter=${filter}`)
  }

  groupByStar(filter: string) {
    return this.httpClient.get(`http://${environment.APP_SERVER}/api/game/groupByStar?filter=${filter}`)
  }

  groupByEmotion(filter: string) {
    return this.httpClient.get(`http://${environment.APP_SERVER}/api/game/groupByEmotion?filter=${filter}`)
  }

  groupByLocation(filter: string) {
    return this.httpClient.get(`http://${environment.APP_SERVER}/api/game/groupByLocation?filter=${filter}`)
  }


  // changeState(id: Number, state: Number) {
  //   return this.httpClient.put(`http://${environment.APP_SERVER}/api/game/${id}/state/${state}`, null)
  // }

  markSame(id: number, isSame: boolean) {
    return this.httpClient.put(`http://${environment.APP_SERVER}/api/game/${id}/mark/${isSame}`, null)
  }

  changeEmotion(id: number, emotion: number) {
    return this.httpClient.put(`http://${environment.APP_SERVER}/api/game/${id}/emotion/${emotion}`, null)
  }

  changePlayState(id: number, state: number) {
    return this.httpClient.put(`http://${environment.APP_SERVER}/api/game/${id}/playstate/${state}`, null)
  }


  changeStar(id: number, star: number) {
    return this.httpClient.put(`http://${environment.APP_SERVER}/api/game/${id}/star/${star}`, null)
  }


  changeLocation(id: number, location: number) {
    return this.httpClient.put(`http://${environment.APP_SERVER}/api/game/${id}/location/${location}`, null)
  }

  blockAll(brandId: number) {
    return this.httpClient.put(`http://${environment.APP_SERVER}/api/game/block/${brandId}`, null)
  }

  normalAll(brandId: number) {
    return this.httpClient.put(`http://${environment.APP_SERVER}/api/game/normal/${brandId}`, null)
  }


  setCharCV(id: number, index: number, cv: string) {
    return this.httpClient.put(`http://${environment.APP_SERVER}/api/game/${id}/gamechar/${index}/cv`, {"cv": cv})
  }


  setMan(id: number, index: number) {
    return this.httpClient.put(`http://${environment.APP_SERVER}/api/game/${id}/gamechar/${index}/setMan`, null)
  }

  setTitle(id: number, mainTitle: string, subTitle: string) {
    return this.httpClient.put(`http://${environment.APP_SERVER}/api/game/${id}/title`,
      {
        "mainTitle": mainTitle,
        "subTitle": subTitle
      }
    )
  }


}
