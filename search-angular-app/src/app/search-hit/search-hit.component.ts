import { Component, OnInit, Input } from '@angular/core';

import { SearchHit } from '../searchHit';

@Component({
  selector: 'app-search-hit',
  templateUrl: './search-hit.component.html',
  styleUrls: ['./search-hit.component.css']
})
export class SearchHitComponent implements OnInit {

  @Input() searchHit: SearchHit;

  constructor() { }

  ngOnInit(): void {
  }

}
