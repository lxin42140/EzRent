import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';

const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json' })
};

@Injectable({
  providedIn: 'root'
})
export class DeliveryService {

  baseUrl: string = "/api/Delivery";


  constructor(private httpClient: HttpClient) {

  }

  // getRecords(): Observable<Record[]> {
  //   return this.httpClient.get<Record[]>(this.baseUrl + "/retrieveAllRecords").pipe
  //     (
  //       catchError(this.handleError)
  //     );
  // }

  // createNewRecord(newRecord: Record): Observable<number> {
  //   return this.httpClient.put<number>(this.baseUrl, newRecord, httpOptions).pipe
  //     (
  //       catchError(this.handleError)
  //     );
  // }

  private handleError(error: HttpErrorResponse) {
    let errorMessage: string = "";

    if (error.error instanceof ErrorEvent) {
      errorMessage = "An unknown error has occurred: " + error.error;
    }
    else {
      errorMessage = "A HTTP error has occurred: " + `HTTP ${error.status}: ${error.error}`;
    }

    console.error(errorMessage);

    return throwError(errorMessage);
  }
}
