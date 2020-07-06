import { Component } from '@angular/core';
import { SearchHit } from './searchHit';
import { SearchService } from './search.service';
import {MessageService} from 'primeng/api';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
  providers: [MessageService]
})
export class AppComponent {

  searchHits: SearchHit[] = [];
  query: string = "";
  searching: boolean = false;

  constructor(private searchService: SearchService, private messageService: MessageService) { }

  searchTriggered(query:string){
    this.searching = true;
    this.searchHits = [];
    console.log("Search triggered for '" + query + "'");
    this.searchService.search(query).subscribe(
      data => {
        this.query = query;
        this.searchHits = data;
        console.log("Found", this.searchHits.length, "search hits for", this.query, this.searchHits);
        this.searching = false;
      },
      err => {
        this.searchHits = [];
        this.searching = false;
        this.showError(err);
      }
    );
  }

  showError(errorMsg: string) {
    this.messageService.add({severity:'error', summary: 'Oops...', detail:errorMsg});
  }

  title = 'Search Web UI';
}
