import { TagService } from '../services/tag.service';
import { ConfirmationService } from 'primeng/api';

import {
  SessionService
} from '../services/session.service';
import { Tag } from '../models/tag';
import { CreateTagReq } from '../models/createTagReq';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-tag-management',
  templateUrl: './tag-management.component.html',
  styleUrls: ['./tag-management.component.css'],
  providers: [ConfirmationService]
})
export class TagManagementComponent implements OnInit {

  tags: Tag[];

  //update tag
  updatedTagName: string;
  updateTagDialogue: boolean;
  updateTagId: number | undefined;

  //create tag
  newTagName: string;
  createTagDialogue: boolean;

  //delete tag
  deleteTagId: number | undefined;
  deleteTagDialogue: boolean;

  //error
  hasError: boolean;
  errorMessage: string | undefined;

  constructor(private tagService: TagService, private sessionService: SessionService, private confirmationService: ConfirmationService) {
    this.tags = [];
    this.hasError = false;
    this.newTagName = "";
    this.updatedTagName = "";
    this.createTagDialogue = false;
    this.updateTagDialogue = false;
    this.deleteTagDialogue = false;
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

  setTagToUpdate(tagId: number): void {
    this.updateTagId = tagId;
    this.updateTagDialogue = !this.updateTagDialogue;
    this.hasError = false;
    this.newTagName = "";
  }

  updateTag(): void {
    if (this.updatedTagName.length > 0 && this.updateTagId !== undefined) {
      this.tagService.updateTagName(this.updateTagId, this.updatedTagName).subscribe(response => {
        this.tags = this.tags.filter(x => x.tagId !== this.updateTagId);
        this.tags.push(response);
        this.updatedTagName = "";
        this.updateTagDialogue = false;
      }, error => {
        this.hasError = true;
        this.errorMessage = error;
      })
    }
  }

  handleShowCreateTagDialogue(): void {
    this.createTagDialogue = !this.createTagDialogue;
    this.hasError = false;
    this.newTagName = "";
  }

  createTag(): void {
    if (this.newTagName.length > 0) {
      let newTag = new Tag(this.newTagName);
      let createTagRequest = new CreateTagReq(this.sessionService.getUsername(), this.sessionService.getPassword(), newTag);
      this.tagService.createNewTag(createTagRequest).subscribe(response => {
        let arrayCopy = JSON.parse(JSON.stringify(this.tags));
        arrayCopy.push(response);
        this.tags = arrayCopy;
        this.newTagName = "";
        this.createTagDialogue = false;
      },
        error => {
          this.hasError = true;
          this.errorMessage = error;
        })
    }
  }

  setTagToDelete(tagId: number): void {
    this.deleteTagId = tagId;
    this.deleteTagDialogue = !this.deleteTagDialogue;
    this.hasError = false;
    this.newTagName = "";
  }

  deleteTag(): void {
    if (this.deleteTagId !== undefined) {
      this.tagService.deleteTag(this.deleteTagId).subscribe(response => {
        this.tags = this.tags.filter(x => x.tagId !== this.deleteTagId);
        this.deleteTagId = undefined;
        this.deleteTagDialogue = false;
      },
        error => {
          this.hasError = true;
          this.errorMessage = error;
        })
    }
  }
}

