import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, of, throwError } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';
import { SearchHit } from './searchHit';
import { environment } from '../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class SearchService {

  private apiUrl: string;

  constructor(private http: HttpClient) {
    this.apiUrl = environment.apiUrl;
  }

  getTerms(prefix: string): Observable<any>{
    if (!prefix.trim()) {
      // if not search term, return empty hero array.
      return of([]);
    }

    return this.http.get<any>(this.apiUrl + "vocabulary?prefix=" + prefix).pipe(
      catchError((err) => this.handleError(err, "Unable to get terms from the server. See log for details."))
    );
  }

  search(query: string): Observable<SearchHit[]>{
    if(!query.trim()){
      return of(<SearchHit[]>[]);
    }

    return this.http.get<SearchHit[]>(this.apiUrl + "search?query=" + query).pipe(
      catchError((err) => this.handleError(err, "Unable to search on the server. See log for details."))
    );
  }

  private handleError(error: HttpErrorResponse, msg: string) {
    if (error.error instanceof ErrorEvent) {
      // A client-side or network error occurred. Handle it accordingly.
      console.error('An error occurred:', error.error.message);
    } else {
      // The backend returned an unsuccessful response code.
      // The response body may contain clues as to what went wrong,
      console.error(
        `Backend returned code ${error.status}, ` +
        `body was: ${error.error}`);
    }
    // return an observable with a user-facing error message
    return throwError(msg);
  };

}
