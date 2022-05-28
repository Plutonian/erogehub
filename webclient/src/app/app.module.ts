import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';

import {AppComponent} from './app.component';
import {DetailComponent} from './game/detail/detail.component';
import {StarComponent} from './star/star.component';
import {BangumiPipl, ImgUrlPipl, LocationPipl, StatePipl, WikiPipl} from "./imgUrl.pipl";
import {HttpClientModule} from "@angular/common/http";
import {GameService} from "./game/game.service";
import {InfoComponent} from './game/detail/info/info.component';
import {GameCharactersComponent} from './game/detail/game-characters/game-characters.component';
import {GuideComponent} from './guide/guide.component';
import {Route, RouterModule} from "@angular/router";
import {GridCellComponent} from './game/grid-cell/grid-cell.component';
import {JumpBrandComponent} from './game/part/jump-brand/jump-brand.component';
import {SearchComponent} from './game/search/search.component';
import {QueryComponent} from './game/query/query.component';
import {ExplorerComponent} from "./game/explorer.component";
import {FormsModule} from "@angular/forms";
import {StarShowComponent} from './game/part/star-show/star-show.component';
import {CvComponent} from './cv/cv.component';
import {TagComponent} from './tag/tag.component';
import {BrandComponent} from './brand/brand.component';
import {JumpcvComponent} from './game/part/jumpcv/jumpcv.component';
import {JumptagComponent} from './game/part/jumptag/jumptag.component';
import {StateChangeComponent} from './game/part/state-change/state-change.component';
import {LocationShowComponent} from './game/part/location-show/location-show.component';
import {LocationChangeComponent} from './game/part/location-change/location-change.component';
import {StarChangeComponent} from './game/part/star-change/star-change.component';
import {GameComponent} from './game/game.component';
import {ByStarComponent} from './game/by-star/by-star.component';
import {ByTagComponent} from './game/by-tag/by-tag.component';
import {ByCVComponent} from './game/by-cv/by-cv.component';
import {ByTruecvComponent} from './game/by-truecv/by-truecv.component';
import {ByStateComponent} from './game/by-state/by-state.component';
import {ByLocationComponent} from './game/by-location/by-location.component';

export const IMG_SERVER = "192.168.2.236"
export const APP_SERVER = "192.168.2.214:9000"

const routes: Route[] = [
  {path: 'cv', component: CvComponent},
  {path: 'tag', component: TagComponent},
  {path: 'brand', component: BrandComponent},
  {
    path: 'game', component: GameComponent, children: [
      {path: 'search', component: SearchComponent},
      {path: 'star/:star', component: ByStarComponent},
      {path: 'tag/:tag', component: ByTagComponent},
      {path: 'state/:state', component: ByStateComponent},
      {path: 'location/:location', component: ByLocationComponent},
      {path: 'cv/:cv', component: ByCVComponent},
      {path: 'truecv/:truecv', component: ByTruecvComponent},
      {path: 'query/:filter', component: QueryComponent},
      {path: ':id', component: DetailComponent}
    ]
  },

]

@NgModule({
  declarations: [
    ImgUrlPipl,
    WikiPipl,
    BangumiPipl,
    StatePipl,
    LocationPipl,
    AppComponent,
    DetailComponent,
    StarComponent,
    InfoComponent,
    GameCharactersComponent,
    GuideComponent,
    ExplorerComponent,
    GridCellComponent,
    JumpBrandComponent,
    SearchComponent,
    QueryComponent,
    StarShowComponent,
    CvComponent,
    TagComponent,
    BrandComponent,
    JumpcvComponent,
    JumptagComponent,
    StateChangeComponent,
    LocationShowComponent,
    LocationChangeComponent,
    StarChangeComponent,
    GameComponent,
    ByStarComponent,
    ByTagComponent,
    ByCVComponent,
    ByTruecvComponent,
    ByStateComponent,
    ByLocationComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    RouterModule.forRoot(routes),
    FormsModule
  ],
  providers: [GameService],
  bootstrap: [AppComponent]
})
export class AppModule {
}
