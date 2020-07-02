import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';

import { AppComponent } from './app.component';
import { SearchBarComponent } from './search-bar/search-bar.component';
import { SearchHitComponent } from './search-hit/search-hit.component';
import { SearchHitListComponent } from './search-hit-list/search-hit-list.component';

@NgModule({
  declarations: [
    AppComponent,
    SearchBarComponent,
    SearchHitComponent,
    SearchHitListComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpClientModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
