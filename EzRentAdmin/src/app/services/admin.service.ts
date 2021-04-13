import { Admin } from './../models/admin';
import { SessionService } from './session.service';
import { Injectable } from '@angular/core';

import { HttpClient, HttpHeaders, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { CreateAdminReq } from '../models/create-admin-req';

const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json' })
};

@Injectable({
  providedIn: 'root'
})
export class AdminService {

  baseUrl: string = "/api/Admin";

  constructor(private httpClient: HttpClient,
    private sessionService: SessionService) { }

  adminLogin(username: string, password: string): Observable<Admin> {
    return this.httpClient.get<Admin>(this.baseUrl + "/adminLogin?username=" + username + "&password=" + password).pipe
      (
        catchError(this.handleError)
      );
  }

  createNewAdmin(createAdminReq: CreateAdminReq): Observable<number>
  {		
		return this.httpClient.put<number>(this.baseUrl + "/createAdminAcc", createAdminReq, httpOptions).pipe
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
