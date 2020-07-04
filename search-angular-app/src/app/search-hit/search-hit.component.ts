import { Component, OnInit, Input } from '@angular/core';
import { SearchHit } from '../searchHit';
import { HighlightService } from '../highlight.service';

@Component({
  selector: 'app-search-hit',
  templateUrl: './search-hit.component.html',
  styleUrls: ['./search-hit.component.css']
})
export class SearchHitComponent implements OnInit {

  @Input() searchHit: SearchHit;

  highlightedSnippet: string = "";

  constructor(private highlightService: HighlightService) { }

  ngOnInit(): void {
    this.highlightedSnippet = this.highlightService.highlight(this.searchHit.snippet);
  }

}
