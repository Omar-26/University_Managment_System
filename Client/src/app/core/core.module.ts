import { NgModule, Optional, SkipSelf } from '@angular/core';

@NgModule({
  providers: [
    // Interceptors or global services can go here, or use providedIn: 'root'
  ],
})
export class CoreModule {
  constructor(@Optional() @SkipSelf() parentModule: CoreModule) {
    if (parentModule) {
      throw new Error(
        'CoreModule is already loaded. Import it only in AppModule.'
      );
    }
  }
}
