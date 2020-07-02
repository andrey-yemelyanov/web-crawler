import { Component, OnInit, Input } from '@angular/core';
import { SearchHit } from '../searchHit';

@Component({
  selector: 'app-search-hit-list',
  templateUrl: './search-hit-list.component.html',
  styleUrls: ['./search-hit-list.component.css']
})
export class SearchHitListComponent implements OnInit {

  @Input() searchHits: SearchHit[];

  constructor() { }

  ngOnInit(): void {
  }

}
