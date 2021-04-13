import { Category } from '../models/category';
import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { Tag } from '../models/tag';
import { CreateTagReq } from '../models/createTagReq';
import { SessionService } from './session.service';

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

  retrieveAllTags(): Observable<Tag[]> {
    // return this.httpClient.get<Tag[]>(this.baseUrl + "/retrieveAllTags?username="+ this.sessionService.getUsername() + "&password=" + this.sessionService.getPassword()).pipe
    return this.httpClient.get<Tag[]>(this.baseUrl + "/retrieveAllTags?username=admin1&password=password").pipe  
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

  updateTagName(tagId: number, newTagName: string): Observable<Tag> {
    return this.httpClient.post<Tag>(this.baseUrl + "/updateTagName?username=" + this.sessionService.getUsername() + "&password=" + this.sessionService.getPassword() + "&tagId=" + tagId + "&newTagName="+newTagName,undefined).pipe
      (
        catchError(this.handleError)
      );
  }

  deleteTag(tagId: number): Observable<any> {
    return this.httpClient.post<any>(this.baseUrl + "/deleteTag?username=" + this.sessionService.getUsername() + "&password=" + this.sessionService.getPassword() + "&tagId=" + tagId, undefined).pipe
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

