import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';

import { Delivery } from '../models/delivery'
import { UpdateDeliveryReq } from '../models/update-delivery-req'

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

  //RMB TO SET DELIVERY COMPANY ID

  getDeliveries(): Observable<Delivery[]> {
    return this.httpClient.get<Delivery[]>(this.baseUrl + "/retrieveAllDeliveries?deliveryCompanyId=" + sessionStorage.getDeliveryCompanyId()).pipe
      (
        catchError(this.handleError)
      );
  }

  createNewDelivery(newDelivery: Delivery): Observable<number> {
    return this.httpClient.put<number>(this.baseUrl, newDelivery, httpOptions).pipe
      (
        catchError(this.handleError)
      );
  }

  //NEED TO TEST IF THIS WORKS!!
  updateDelivery(deliveryId : number, deliveryStatus: string): Observable<any>
    {
      // let updateDeliveryReq: UpdateDeliveryReq = new UpdateDeliveryReq(sessionStorage.getDeliveryCompanyId(), transactionId, deliveryToUpdate);
      
      return this.httpClient.post<any>(this.baseUrl, {"deliveryId": deliveryId, "deliveryStatus" : deliveryStatus}, httpOptions).pipe
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
