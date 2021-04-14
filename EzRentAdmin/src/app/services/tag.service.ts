import { CreateTagReq } from './../models/createTagReq';
import { SessionService } from './session.service';
import { Tag } from '../models/tag';
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
export class TagService {

  baseUrl: string = "/api/Tag";

  constructor(private httpClient: HttpClient, private sessionService: SessionService) {

  }

  getTags(): Observable<Tag[]> {
    return this.httpClient.get<Tag[]>(this.baseUrl + "/retrieveAllTags?username=" + this.sessionService.getUsername() + "&password=" + this.sessionService.getPassword()).pipe
      (
        catchError(this.handleError)
      );
  }

  createNewTag(createTagReq: CreateTagReq): Observable<number> {
    return this.httpClient.put<number>(this.baseUrl + "/createNewTag", createTagReq, httpOptions).pipe
      (
        catchError(this.handleError)
      );
  }

  updateTag(tagId: number, tagName: string): Observable<any> {
    return this.httpClient.post<any>(this.baseUrl + "/updateTagName/" + tagId + "/" + tagName + "?username=" + this.sessionService.getUsername() + "&password=" + this.sessionService.getPassword(), httpOptions).pipe
      (
        catchError(this.handleError)
      );
  }

  deleteTag(tagId: number): Observable<any> {
    return this.httpClient.delete<any>(this.baseUrl + "/" + tagId + "?username=" + this.sessionService.getUsername() + "&password=" + this.sessionService.getPassword()).pipe
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

