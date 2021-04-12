import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';

import { DeliveryCompany } from '../models/delivery-company'

// const httpOptions = {
//   headers: new HttpHeaders({ 'Content-Type': 'application/json' })
// };

@Injectable({
  providedIn: 'root'
})


export class DeliveryCompanyService {

  baseUrl: string = "/api/DeliveryCompany";

  constructor(private httpClient: HttpClient) { }

  deliveryCompanyLogin(username: string | undefined, password: string | undefined) : Observable<DeliveryCompany> {
    return this.httpClient.get<DeliveryCompany>(this.baseUrl + "?username=" + username + "&password=" + password).pipe
		(
			catchError(this.handleError)
		);
  }

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
