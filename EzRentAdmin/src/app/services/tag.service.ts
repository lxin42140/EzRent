import { Category } from '../models/category';
import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { Tag } from '../models/tag';
import { CreateTagReq } from '../models/createTagReq';

const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json' })
};


@Injectable({
  providedIn: 'root'
})
export class TagService {

  baseUrl: string = "/api/Tag";

  constructor(private httpClient: HttpClient) {

  }

  retrieveAllTags(): Observable<Tag[]> {
    return this.httpClient.get<Tag[]>(this.baseUrl + "/retrieveAllTags").pipe
      (
        catchError(this.handleError)
      );
  }

  createNewTag(createTagReq: CreateTagReq): Observable<Tag> {
    return this.httpClient.put<Tag>(this.baseUrl, createTagReq, httpOptions).pipe
      (
        catchError(this.handleError)
      );
  }

  updateTagName(username: string, password: string, tagId: number, newTagName: string): Observable<Tag> {
    return this.httpClient.post<Tag>(this.baseUrl + "/updateTagName?username=" + username + "&password=" + password + "&tagId=" + tagId + "&newTagName="+newTagName,undefined).pipe
      (
        catchError(this.handleError)
      );
  }

  deleteTag(username: string, password: string, tagId: number): Observable<any> {
    return this.httpClient.post<any>(this.baseUrl + "/deleteTag?username=" + username + "&password=" + password + "&tagId=" + tagId, undefined).pipe
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

