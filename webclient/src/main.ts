import {enableProdMode} from '@angular/core';
import {platformBrowserDynamic} from '@angular/platform-browser-dynamic';

import {AppModule} from './app/app.module';
import {environment} from './environments/environment';

if (environment.production) {
  enableProdMode();
}

platformBrowserDynamic().bootstrapModule(AppModule)
  .catch(err => console.error(err));

export class Arrays {
  static range = function (from: Number, to: Number) {
    const a = []
    // @ts-ignore
    for (let i = from; i < to; i++)
      a.push(i)

    return a

  }

  static rangeInclude = function (from: Number, to: Number) {
    const a = []
    // @ts-ignore
    for (let i = from; i <= to; i++)
      a.push(i)

    return a

  }
}
