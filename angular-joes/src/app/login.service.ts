import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders, HttpParams} from '@angular/common/http';
import {Observable, of} from 'rxjs';
import {catchError, map, tap} from 'rxjs/operators';
import {Product} from './product'

@Injectable({
  providedIn: 'root'
})
export class LoginService {

  private loginUrl = 'http://localhost:8080/login';

  private username = '';

  httpOptions = {
    headers: new HttpHeaders({'Content-Type': 'application/json'})
  };

  constructor(private http: HttpClient) { 
  
  }

  setCurrentUser(username: string) {
    this.username = username;
  }

  getCurrentUser(): string {
    return this.username;
  }

  //If user exists, return cart (array of products), else create user, and return empty cart
  /**
   * 
   * @param username username of cart to fetch (cart is array of products)
   * @returns Array of products, 'cart'
   */
  getUserCart(username: string): Observable<Product[]> {
    const url = `${this.loginUrl}/${username}`;
    return this.http.get<Product[]>(url).pipe(
      tap(_ => console.log(`fetched cart`)),
      catchError(this.handleError<Product[]>(`getUserCart username=${username}`, []))
    );
  }

  /**
   * 
   * @param cart cart to change to in database
   * @returns returns result to prove it worked
   */
  updateUserCart(username: string, newCart: Product[]): Observable<any> {
    const url = `${this.loginUrl}/${username}`;
    return this.http.put(url, newCart, this.httpOptions).pipe(
      tap(_ => console.log(`updated user=${username}`)),
      catchError(this.handleError<any>('updateProduct'))
    );
  }

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
}
