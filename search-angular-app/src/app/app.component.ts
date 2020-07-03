import { Component } from '@angular/core';
import { SearchHit } from './searchHit';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {

  searchHits: SearchHit[] = [];

  constructor() { 
    this.searchHits = [
      new SearchHit("title1", "http://localhost/title1"),
      new SearchHit("title2", "http://localhost/title2"),
      new SearchHit("title3", "http://localhost/title3")
    ];
  }

  searchTriggered(event){
    alert(event);
  }

  title = 'Search Web UI';
}
