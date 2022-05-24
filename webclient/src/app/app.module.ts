import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';

import {AppComponent} from './app.component';
import {DetailComponent} from './game/detail/detail.component';
import {StarComponent} from './star/star.component';
import {ImgUrlPipl} from "./imgUrl.pipl";
import {HttpClientModule} from "@angular/common/http";
import {GameService} from "./game/game.service";
import {InfoComponent} from './game/detail/info/info.component';
import {GameCharactersComponent} from './game/detail/game-characters/game-characters.component';
import {GuideComponent} from './guide/guide.component';
import {Route, RouterModule} from "@angular/router";


const routes: Route[] = [
  {path: 'game/:id', component: DetailComponent}
]

@NgModule({
  declarations: [
    AppComponent,
    DetailComponent,
    StarComponent,
    ImgUrlPipl,
    InfoComponent,
    GameCharactersComponent,
    GuideComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    RouterModule.forRoot(routes)
  ],
  providers: [GameService],
  bootstrap: [AppComponent]
})
export class AppModule {
}
