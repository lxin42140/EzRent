import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';

import { Delivery } from '../models/delivery'
import { UpdateDeliveryReq } from '../models/update-delivery-req'
import { CreateDeliveryReq } from '../models/create-delivery-req';
import { SessionService } from './session.service';
const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json' })
};

@Injectable({
  providedIn: 'root'
})
export class DeliveryService {

  baseUrl: string = "/api/Delivery";


  constructor(private httpClient: HttpClient, private sessionService: SessionService) {
  }

  getDeliveries(): Observable<Delivery[]> {
    console.log(this.sessionService.getCurrentDeliveryCompany().userId);
    return this.httpClient.get<Delivery[]>(this.baseUrl + "/?deliveryCompanyId=" + this.sessionService.getCurrentDeliveryCompany().userId).pipe
      (
        catchError(this.handleError)
      );
  }

  createNewDelivery(newDeliveryReq: CreateDeliveryReq): Observable<number> {
    return this.httpClient.put<number>(this.baseUrl, newDeliveryReq, httpOptions).pipe
      (
        catchError(this.handleError)
      );
  }

  updateDelivery(updateDeliveryReq: UpdateDeliveryReq): Observable<any> {
    return this.httpClient.post<any>(this.baseUrl + "/updateDeliveryStatus", updateDeliveryReq, httpOptions).pipe
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
