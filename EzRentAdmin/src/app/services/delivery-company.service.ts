import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';

import { DeliveryCompany } from '../models/delivery-company'
import { CreateDeliveryCompanyReq } from '../models/CreateDeliveryCompanyReq';


const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json' })
};

@Injectable({
  providedIn: 'root'
})
export class DeliveryCompanyService {

  baseUrl: string = "/api/Admin";

  constructor(private httpClient: HttpClient) {

  }

  createNewDeliveryCompany(createDeliveryCompanyReq: CreateDeliveryCompanyReq): Observable<number>
  {		
		return this.httpClient.put<number>(this.baseUrl + "/createDeliveryAcc", createDeliveryCompanyReq, httpOptions).pipe
		(
			catchError(this.handleError)
		);
  }

  retrieveAllDeliveryCompanies(username: string, password: string): Observable<DeliveryCompany[]>
  {
		return this.httpClient.put<DeliveryCompany[]>(this.baseUrl + "/retrieveAllDeliveryCompanies?username=" + username + "&password=" + password, httpOptions).pipe
		(
			catchError(this.handleError)
		);  
  }


  updateDeliveryCompanyStatus(username: string, password: string, deliveryCompanyId: number, isDisabled: boolean): Observable<DeliveryCompany>
  {
		return this.httpClient.put<DeliveryCompany>(this.baseUrl + "/updateDeliveryCompanyStatus?username=" + username + "&password=" + password + "&deliveryCompanyId=" + deliveryCompanyId + "&isDisabled=" + isDisabled, httpOptions).pipe
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
