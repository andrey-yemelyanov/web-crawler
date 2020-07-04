import {Snippet} from '../app/snippet';

export class SearchHit {
    constructor(
        public title: string,
        public url: string,
        public snippet: Snippet
    ){}
}
