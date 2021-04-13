import { TagService } from '../services/tag.service';
import {
  SessionService
} from '../services/session.service';
import { Tag } from '../models/tag';
import { CreateTagReq } from '../models/createTagReq';
import { Component, OnInit } from '@angular/core';
@Component({
  selector: 'app-tag-management',
  templateUrl: './tag-management.component.html',
  styleUrls: ['./tag-management.component.css']
})
export class TagManagementComponent implements OnInit {

  tags: Tag[];

  //update tag
  updatedTagName: string;

  //create tag
  newTagName: string;

  //error
  hasError: boolean;
  errorMessage: string | undefined;

  constructor(private tagService: TagService, private sessionService: SessionService) {
    this.tags = [];
    this.hasError = false;
    this.newTagName = "";
    this.updatedTagName = "";
  }

  ngOnInit(): void {
    this.tagService.retrieveAllTags().subscribe(response => {
      this.tags = response;
    },
      error => {
        this.hasError = true;
        this.errorMessage = error;
      })
  }

  updateTag(tagId: number): void {
    if (this.updatedTagName.length > 0) {
      this.tagService.updateTagName(tagId, this.updatedTagName).subscribe(response => {
        this.tags = this.tags.filter(x => x.getTagId() !== tagId);
        this.tags.push(response);
        this.updatedTagName = "";
      }, error => {
        this.hasError = true;
        this.errorMessage = error;
      })
    }
  }

  createTag(): void {
    if (this.newTagName.length > 0) {
      let newTag = new Tag(this.newTagName);
      let createTagRequest = new CreateTagReq(this.sessionService.getUsername(), this.sessionService.getPassword(), newTag);
      this.tagService.createNewTag(createTagRequest).subscribe(response => {
        this.tags.push(response);
        this.newTagName = "";
      },
        error => {
          this.hasError = true;
          this.errorMessage = error;
        })
    }
  }

  deleteTag(tagId: number): void {
    this.tagService.deleteTag(tagId).subscribe(response => {
      this.tags = this.tags.filter(x => x.getTagId() !== tagId);
    },
      error => {
        this.hasError = true;
        this.errorMessage = error;
      })
  }

}
