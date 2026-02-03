import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, NavigationEnd, Router} from "@angular/router";
import { HttpClient } from "@angular/common/http";
import {DateGroupItem} from "./entity";
import {environment} from "../environments/environment";
import {Title} from "@angular/platform-browser";
import {filter} from "rxjs";


@Component({
    selector: 'app-root',
    templateUrl: './app.component.html',
    styleUrls: ['./app.component.css'],
    standalone: false
})
export class AppComponent  {

}
