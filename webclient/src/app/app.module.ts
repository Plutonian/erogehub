import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';

import {AppComponent} from './app.component';
import {DetailComponent} from './game/detail/detail.component';
import {BangumiPipl, BlockPipl, EmotionJPPipe, ImgUrlPipl, LocationPipl, PlayStatePipl, WikiPipl} from "./imgUrl.pipl";
import {HttpClientModule} from "@angular/common/http";
import {GameService} from "./game/game.service";
import {GameCharactersComponent} from './game/detail/game-characters/game-characters.component';
import {Route, RouterModule} from "@angular/router";
import {JumpBrandComponent} from './game/part/jump-brand/jump-brand.component';
import {GameSearchComponent} from './game/search/game-search.component';
import {QueryComponent} from './game/query/query.component';
import {FormsModule} from "@angular/forms";
import {CvComponent} from './cv/cv.component';
import {TagComponent} from './tag/tag.component';
import {JumpcvComponent} from './game/part/jumpcv/jumpcv.component';
import {JumptagComponent} from './game/part/jumptag/jumptag.component';
import {GameMarkSameComponent} from './game/part/mark-same/game-mark-same.component';
import {LocationShowComponent} from './game/part/location-show/location-show.component';
import {LocationChangeComponent} from './game/part/location-change/location-change.component';
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
  ToggleModule,
  TreeModule
} from "ng-devui";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {MonthCalendarComponent} from './common/month-calendar/month-calendar.component';
import {RelativeTimeModule} from "ng-devui/relative-time";
import {FormattedDateComponent} from './common/formatted-date/formatted-date.component';
import {StarChangeComponent} from './game/part/star-change/star-change.component';
import {GroupsideComponent} from './game/explorer/groupside/groupside.component';
import {GroupBrandComponent} from './game/explorer/groupside/group-brand/group-brand.component';
import {GameListPanelComponent} from './game/explorer/groupside/game-list-panel/game-list-panel.component';
import {GameDetailMainComponent} from './game/detail/main/game-detail-main.component';
import {AppService} from "./app.service";
import {PlaystateChangeComponent} from './game/part/playstate-change/playstate-change.component';
import {EmotionChangeComponent} from './game/part/emotion-change/emotion-change.component';
import {ExplorerComponent} from "./game/explorer/explorer.component";
import {ByStarComponent} from "./game/query/by-star/by-star.component";
import {ByTagComponent} from "./game/query/by-tag/by-tag.component";
import {ByPainterComponent} from "./game/query/by-painter/by-painter.component";
import {ByEmotionComponent} from "./game/query/by-emotion/by-emotion.component";
import {ByPlaystateComponent} from "./game/query/by-playstate/by-playstate.component";
import {ByLocationComponent} from "./game/query/by-location/by-location.component";
import {ByCVComponent} from "./game/query/by-cv/by-cv.component";
import {ByTruecvComponent} from "./game/query/by-truecv/by-truecv.component";
import {GridCellComponent} from "./game/explorer/cell/grid-cell/grid-cell.component";
import {FilterComponent} from "./game/explorer/filter/filter.component";
import {ListCellComponent} from "./game/explorer/cell/list-cell/list-cell.component";


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
      {path: 'emotion/:emotion', component: ByEmotionComponent},
      {path: 'playState/:playState', component: ByPlaystateComponent},
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
    PlayStatePipl,
    LocationPipl,
    EmotionJPPipe,
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
    GameMarkSameComponent,
    LocationShowComponent,
    LocationChangeComponent,
    ByStarComponent,
    ByTagComponent,
    ByCVComponent,
    ByTruecvComponent,
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
    ByPainterComponent,
    PlaystateChangeComponent,
    ByPlaystateComponent,
    ByEmotionComponent,
    EmotionChangeComponent
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
    PopoverModule,
    ToggleModule
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
