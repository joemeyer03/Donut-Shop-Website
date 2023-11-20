import { Injectable } from '@angular/core';
import { Review } from './review';
import { Observable, of } from 'rxjs';

import { HttpClient, HttpHeaders } from '@angular/common/http';
import { catchError, map, tap } from 'rxjs/operators';

@Injectable({
  providedIn: 'root',
})
export class ReviewService {
  private reviewsUrl = 'http://localhost:8080/reviews'

  constructor(private http: HttpClient) {
  }

  /** GET products from the server */
  getAllReviews(): Observable<Review[]> {
    return this.http.get<Review[]>(this.reviewsUrl)
      .pipe(
        tap(_ => console.log('fetched products')),
        catchError(this.handleError<Review[]>('getAllReviews', []))
      );
  }

  /** GET product by id. Will 404 if id not found */
  getReviews(name: string): Observable<Review> {
    const url = `${this.reviewsUrl}/${name}`;
    return this.http.get<Review>(url).pipe(
      tap(_ => console.log(`fetched reviews name=${name}`)),
      catchError(this.handleError<Review>(`getReviews name=${name}`))
    );
  }

  /**
   * Handle Http operation that failed.
   * Let the app continue.
   *
   * @param operation - name of the operation that failed
   * @param result - optional value to return as the observable result
   */
  private handleError<T>(operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {

      // TODO: send the error to remote logging infrastructure
      console.error(error); // log to console instead

      // TODO: better job of transforming error for user consumption
      console.log(`${operation} failed: ${error.message}`);

      // Let the app keep running by returning an empty result.
      return of(result as T);
    };
  }

  /** POST: add a new product to the server */
  addReview(review: Review): Observable<Review> {
    return this.http.post<Review>(this.reviewsUrl, review, this.httpOptions).pipe(
      tap((newReview: Review) => console.log(`added review w/ name=${newReview.name}`)),
      catchError(this.handleError<Review>('addReview'))
    );
  }

  httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };
}