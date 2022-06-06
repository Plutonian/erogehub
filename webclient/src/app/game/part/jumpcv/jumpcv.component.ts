import {Component, Input, OnInit} from '@angular/core';
import {Router} from "@angular/router";

@Component({
  selector: 'app-jumpcv',
  templateUrl: './jumpcv.component.html',
  styleUrls: ['./jumpcv.component.css']
})
export class JumpcvComponent implements OnInit {

  @Input()
  cv: string

  @Input()
  isReal: Boolean

  constructor(private router: Router) {
  }


  ngOnInit(): void {
  }

}
