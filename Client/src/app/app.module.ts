import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { CoreModule } from './core/core.module';

@NgModule({
  declarations: [AppComponent],
  imports: [BrowserModule, AppRoutingModule, CoreModule],
  providers: [],
  bootstrap: [AppComponent],
})
export class AppModule {}

// core → single-use providers only, imported once in AppModule. Put auth interceptors, guards, global services here.

// shared → reusable UI pieces, imported where needed. common components, pipes, directives import this in feature modules

// features → isolated business modules with routing and services.

// Lazy load large feature modules with routing for faster load time.
