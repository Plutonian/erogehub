import {Component, OnInit} from '@angular/core';
import {Part, Token, WordService} from "./word.service";

@Component({
  selector: 'app-word',
  templateUrl: './word.component.html',
  styleUrls: ['./word.component.css']
})
export class WordComponent implements OnInit {

  constructor(private service: WordService) {
  }

  word: string

  // tokens: Token[]
  parts: Part[]

  ngOnInit(): void {
  }

  search() {
    this.service.query(this.word)
      .subscribe((parts: Part[]) => {
          this.parts = parts

          // console.log("parts", parts);

        }
      )
  }

}
