import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';

import {AppComponent} from './app.component';
import {DetailComponent} from './game/detail/detail.component';
import {
  BangumiPipl,
  BlockPipl,
  EmotionJPPipe,
  ImgUrlPipl,
  LocationPipl,
  MyDatePipl,
  PlayStatePipl,
  WikiPipl
} from "./imgUrl.pipl";
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
import {NzMenuModule} from 'ng-zorro-antd/menu';
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {MonthCalendarComponent} from './common/month-calendar/month-calendar.component';
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
import {ByDateComponent} from './game/query/by-date/by-date.component';
import {GameSimpleComponent} from './game/detail/simple/game-simple.component';
import {GameListViewComponent} from './game/explorer/gamelist/game-list-view.component';
import {NzSwitchModule} from 'ng-zorro-antd/switch';
import {NzToolTipModule} from 'ng-zorro-antd/tooltip';
import {NzAnchorModule} from 'ng-zorro-antd/anchor';
// import {ja_JP, NZ_I18N} from 'ng-zorro-antd/i18n';
// import {registerLocaleData} from '@angular/common';
// import ja from '@angular/common/locales/ja';
import {NzCollapseModule} from "ng-zorro-antd/collapse";
import {NzBreadCrumbModule} from "ng-zorro-antd/breadcrumb";
import {NzRadioModule} from "ng-zorro-antd/radio";
import {NzFormModule} from "ng-zorro-antd/form";
import {NzPopoverModule} from 'ng-zorro-antd/popover';
import {NzRateModule} from "ng-zorro-antd/rate";
import {NzDropDownModule} from "ng-zorro-antd/dropdown";
import {NzTabsModule} from "ng-zorro-antd/tabs";
import {NzCarouselModule} from "ng-zorro-antd/carousel";
import {NzImageModule} from "ng-zorro-antd/image";
import {NzBadgeModule} from "ng-zorro-antd/badge";
import {NzTagModule} from "ng-zorro-antd/tag";
import {NzLayoutModule} from "ng-zorro-antd/layout";
import {NzCardModule} from "ng-zorro-antd/card";
import {NzListModule} from "ng-zorro-antd/list";
import {NzDescriptionsModule} from "ng-zorro-antd/descriptions";
import {NzTreeModule} from "ng-zorro-antd/tree";
import {NzAffixModule} from "ng-zorro-antd/affix";
import {NzPageHeaderModule} from "ng-zorro-antd/page-header";
import {NzInputModule} from "ng-zorro-antd/input";
import {NzSelectModule} from "ng-zorro-antd/select";
import {NzButtonModule} from "ng-zorro-antd/button";
import {ScrollingModule} from "@angular/cdk/scrolling";
import {NzIconModule} from "ng-zorro-antd/icon";
import {NzSliderModule} from "ng-zorro-antd/slider";
import {ImgCellComponent} from './game/explorer/cell/img-cell/img-cell.component';

// registerLocaleData(ja);


export const routes: Route[] = [
  {
    path: 'cv', component: CvComponent, data: {
      breadcrumb: 'CV'
    }
  },
  {
    path: 'tag', component: TagComponent, data: {
      breadcrumb: 'Tag'
    }
  },
  {
    path: 'brand', data: {
      breadcrumb: 'Brand'
    }, children: [
      {
        path: ':id', component: BrandDetailComponent, data: {
          breadcrumb: 'detail'
        }
      },
      {
        path: '', component: BrandComponent, data: {
          breadcrumb: 'List'
        }
      }
    ]
  },
  {
    path: 'game', data: {
      breadcrumb: 'game'
    }, children: [
      {
        path: 'search', component: GameSearchComponent, data: {
          breadcrumb: 'search'
        }
      },
      {
        path: 'star/:star', component: ByStarComponent, data: {
          breadcrumb: 'star'
        }
      },
      {
        path: 'date/:start/:end', component: ByDateComponent, data: {
          breadcrumb: 'date'
        }
      },
      {
        path: 'tag/:tag', component: ByTagComponent, data: {
          breadcrumb: 'tag'
        }
      },
      {
        path: 'painter/:painter', component: ByPainterComponent, data: {
          breadcrumb: 'painter'
        }
      },
      {
        path: 'emotion/:emotion', component: ByEmotionComponent, data: {
          breadcrumb: 'emotion'
        }
      },
      {
        path: 'playState/:playState', component: ByPlaystateComponent, data: {
          breadcrumb: 'playState'
        }
      },
      {
        path: 'location/:location', component: ByLocationComponent, data: {
          breadcrumb: 'location'
        }
      },
      {
        path: 'cv/:cv', component: ByCVComponent, data: {
          breadcrumb: 'cv'
        }
      },
      {
        path: 'truecv/:truecv', component: ByTruecvComponent, data: {
          breadcrumb: 'truecv'
        }
      },
      {
        path: 'query/:filter', component: QueryComponent, data: {
          breadcrumb: 'filter'
        }
      },
      {
        path: ':id', component: DetailComponent, data: {
          breadcrumb: 'detail'
        }
      }
    ]
  }
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
    MyDatePipl,
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
    EmotionChangeComponent,
    ByDateComponent,
    GameSimpleComponent,
    GameListViewComponent,
    ImgCellComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    HttpClientModule,
    RouterModule.forRoot(routes),
    FormsModule,
    NzMenuModule,
    NzCollapseModule,
    NzBreadCrumbModule,
    NzRadioModule,
    NzFormModule,
    NzSwitchModule,
    NzToolTipModule,
    NzAnchorModule,
    NzPopoverModule,
    NzRateModule,
    NzDropDownModule,
    NzTabsModule,
    NzCarouselModule,
    NzImageModule,
    NzBadgeModule,
    NzTagModule,
    NzLayoutModule,
    NzCardModule,
    NzListModule,
    NzDescriptionsModule,
    NzTreeModule,
    NzAffixModule,
    NzPageHeaderModule,
    NzInputModule,
    NzSelectModule,
    NzButtonModule,
    ScrollingModule,
    NzIconModule,
    NzSliderModule
  ],
  providers: [
    GameService,
    GuideService,
    BrandService,
    AppService
    // {provide: NZ_I18N, useValue: ja_JP}
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
