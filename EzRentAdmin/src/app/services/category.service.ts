import { Category } from '../models/category';
import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { CreateCategoryReq } from '../models/createCategoryReq';
import { CreateCategoryWithParentReq } from '../models/createCategoryWithParentReq';

const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json' })
};


@Injectable({
  providedIn: 'root'
})
export class CategoryService {

  baseUrl: string = "/api/Category";

  constructor(private httpClient: HttpClient) {

  }

  getCategories(): Observable<Category[]> {
    return this.httpClient.get<Category[]>(this.baseUrl + "/retrieveAllCategories").pipe
      (
        catchError(this.handleError)
      );
  }

  createNewRootCategory(createCategoryReq: CreateCategoryReq): Observable<number> {
    return this.httpClient.put<number>(this.baseUrl + "/createNewRootCategory", createCategoryReq, httpOptions).pipe
      (
        catchError(this.handleError)
      );
  }

  createNewRootCategoryWithParentCategory(createCategoryWithParentReq: CreateCategoryWithParentReq): Observable<number> {
    return this.httpClient.put<number>(this.baseUrl + "/createNewRootCategoryWithParentCategory", createCategoryWithParentReq, httpOptions).pipe
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
