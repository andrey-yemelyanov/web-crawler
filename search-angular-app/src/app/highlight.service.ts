import { Injectable } from '@angular/core';
import { Snippet } from './snippet';

interface Offset {
  pos: number;
  term: string;
}

@Injectable({
  providedIn: 'root'
})
export class HighlightService {

  constructor() { }

  highlight(snippet : Snippet): string{

    const text: string = snippet.text;
    const invertedOffsets = this.invert(snippet.offsets);
    if(invertedOffsets.length == 0) return text;

    var offset = invertedOffsets[0];
    var highlightedText: string = text.substr(0, offset.pos);
    highlightedText += "<b>" + text.substr(offset.pos, offset.term.length) + "</b>";
    var fromIndex = offset.pos + offset.term.length;

    for(var i = 1; i < invertedOffsets.length; i++){
      offset = invertedOffsets[i];
      highlightedText += text.substr(fromIndex, offset.pos - fromIndex);
      highlightedText += "<b>" + text.substr(offset.pos, offset.term.length) + "</b>";
      fromIndex = offset.pos + offset.term.length;
    }

    highlightedText += text.substr(fromIndex);

    return highlightedText;
  }

  private invert(offsets: any): Offset[]{
    var invertedOffsets: Offset[] = [];
    Object.keys(offsets).forEach(term => {
      var positions: number[] = offsets[term];
      //console.log("Term", term, "Positions", positions);
      positions.forEach(pos => {
        invertedOffsets.push({
          pos: pos,
          term: term
        });
      });
    });
    invertedOffsets.sort((a, b) => a.pos - b.pos);
    //console.log("Inverted offsets: ", invertedOffsets);
    return invertedOffsets;
  }

}
