import {Component, Input, OnInit, TemplateRef} from '@angular/core';

@Component({
  selector: 'app-panel',
  templateUrl: './panel.component.html',
  styleUrls: ['./panel.component.css']
})
export class PanelComponent implements OnInit {

  @Input()
  head: TemplateRef<any>

  @Input()
  body: TemplateRef<any>


  show: boolean = false

  switch() {
    this.show = !this.show
  }

  constructor() {
  }

  ngOnInit(): void {
  }

}
