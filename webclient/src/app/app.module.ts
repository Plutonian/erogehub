import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';

import {AppComponent} from './app.component';
import {DetailComponent} from './game/detail/detail.component';
import {BangumiPipl, BlockPipl, BrandStatePipl, ImgUrlPipl, LocationPipl, StatePipl, WikiPipl} from "./imgUrl.pipl";
import {HttpClientModule} from "@angular/common/http";
import {GameService} from "./game/game.service";
import {GameCharactersComponent} from './game/detail/game-characters/game-characters.component';
import {Route, RouterModule} from "@angular/router";
import {GridCellComponent} from './game/grid-cell/grid-cell.component';
import {JumpBrandComponent} from './game/part/jump-brand/jump-brand.component';
import {GameSearchComponent} from './game/search/game-search.component';
import {QueryComponent} from './game/query/query.component';
import {ExplorerComponent} from "./game/explorer.component";
import {FormsModule} from "@angular/forms";
import {CvComponent} from './cv/cv.component';
import {TagComponent} from './tag/tag.component';
import {JumpcvComponent} from './game/part/jumpcv/jumpcv.component';
import {JumptagComponent} from './game/part/jumptag/jumptag.component';
import {StateChangeComponent} from './game/part/state-change/state-change.component';
import {LocationShowComponent} from './game/part/location-show/location-show.component';
import {LocationChangeComponent} from './game/part/location-change/location-change.component';
import {ByStarComponent} from './game/by-star/by-star.component';
import {ByTagComponent} from './game/by-tag/by-tag.component';
import {ByCVComponent} from './game/by-cv/by-cv.component';
import {ByTruecvComponent} from './game/by-truecv/by-truecv.component';
import {ByStateComponent} from './game/by-state/by-state.component';
import {ByLocationComponent} from './game/by-location/by-location.component';
import {GuideComponent} from "./game/guide/guide.component";
import {GuideService} from "./game/guide.service";
import {BrandListComponent} from './brand/list/list.component';
import {BrandDetailComponent} from "./brand/detail/detail.component";
import {BrandComponent} from "./brand/brand.component";
import {BrandService} from "./brand/brand.service";
import {
  AnchorModule,
  BadgeModule,
  CarouselModule,
  DevUIModule,
  DrawerModule,
  PanelModule,
  PopoverModule,
  SplitterModule,
  TreeModule
} from "ng-devui";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {MonthCalendarComponent} from './common/month-calendar/month-calendar.component';
import {FilterComponent} from './game/filter/filter.component';
import {ListCellComponent} from './game/list-cell/list-cell.component';
import {RelativeTimeModule} from "ng-devui/relative-time";
import {FormattedDateComponent} from './common/formatted-date/formatted-date.component';
import {StarChangeComponent} from './game/part/star-change/star-change.component';
import {GroupsideComponent} from './game/explorer/groupside/groupside.component';
import {GroupBrandComponent} from './game/explorer/groupside/group-brand/group-brand.component';
import {GameListPanelComponent} from './game/explorer/groupside/game-list-panel/game-list-panel.component';
import {GameDetailMainComponent} from './game/detail/main/game-detail-main.component';
import {AppService} from "./app.service";
import {ByPainterComponent} from './game/by-painter/by-painter.component';

export const IMG_SERVER = "192.168.2.236"
export const APP_SERVER = "localhost:9000"

const routes: Route[] = [
  {path: 'cv', component: CvComponent},
  {path: 'tag', component: TagComponent},
  {
    path: 'brand', children: [
      {path: ':id', component: BrandDetailComponent},
      {path: '', component: BrandComponent}
    ]
  },
  {
    path: 'game', children: [
      {path: 'search', component: GameSearchComponent},
      {path: 'guide/:searchKey', component: GuideComponent},
      {path: 'star/:star', component: ByStarComponent},
      {path: 'tag/:tag', component: ByTagComponent},
      {path: 'painter/:painter', component: ByPainterComponent},
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
    BrandStatePipl,
    BlockPipl,
    AppComponent,
    DetailComponent,
    GameCharactersComponent,
    GuideComponent,
    ExplorerComponent,
    GridCellComponent,
    JumpBrandComponent,
    GameSearchComponent,
    QueryComponent,
    CvComponent,
    TagComponent,
    JumpcvComponent,
    JumptagComponent,
    StateChangeComponent,
    LocationShowComponent,
    LocationChangeComponent,
    ByStarComponent,
    ByTagComponent,
    ByCVComponent,
    ByTruecvComponent,
    ByStateComponent,
    ByLocationComponent,
    BrandListComponent,
    BrandDetailComponent,
    BrandComponent,
    MonthCalendarComponent,
    FilterComponent,
    ListCellComponent,
    FormattedDateComponent,
    StarChangeComponent,
    GroupsideComponent,
    GroupBrandComponent,
    GameListPanelComponent,
    GameDetailMainComponent,
    ByPainterComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    HttpClientModule,
    RouterModule.forRoot(routes),
    FormsModule,
    DevUIModule,
    PanelModule,
    AnchorModule,
    BadgeModule,
    CarouselModule,
    SplitterModule,
    RelativeTimeModule,
    DrawerModule,
    TreeModule,
    PopoverModule
  ],
  providers: [
    GameService,
    GuideService,
    BrandService,
    AppService
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
