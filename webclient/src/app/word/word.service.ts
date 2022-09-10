import {Injectable} from '@angular/core';
import {environment} from "../../environments/environment";
import {HttpClient} from "@angular/common/http";

export interface Part {
  tokens: Token[]
}

export interface Token {
  surface: string
  partOfSpeechLevel1: string
  partOfSpeechLevel2: string
  pronunciation: string
  pronunciationBaseForm: string
  writtenForm: string
  writtenBaseForm: string
  allFeatures: string

}


@Injectable({
  providedIn: 'root'
})
export class WordService {

  constructor(private httpClient: HttpClient) {
  }


  query(word: string) {
    return this.httpClient.post(`http://${environment.APP_SERVER}/api/word`, word)
  }
}
