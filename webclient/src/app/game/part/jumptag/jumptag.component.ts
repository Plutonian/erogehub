import {Component, Input, OnInit} from '@angular/core';
import {Router} from "@angular/router";

@Component({
  selector: 'app-jumptag',
  templateUrl: './jumptag.component.html',
  styleUrls: ['./jumptag.component.css']
})
export class JumptagComponent implements OnInit {

  @Input()
  tag: string

  constructor(private router: Router) {
  }

  ngOnInit(): void {
  }

}
