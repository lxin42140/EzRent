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
  styleUrls: ['./tag-management.component.css']
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
        this.tags = this.tags.filter(x => x.getTagId() !== this.updateTagId);
        this.tags.push(response);
        this.updatedTagName = "";
        this.updateTagId = -1;
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
        this.tags.push(response);
        this.newTagName = "";
      },
        error => {
          this.hasError = true;
          this.errorMessage = error;
        })
    }
  }

  confirmDeleteDialogue(tagId: number): void {
    this.confirmationService.confirm({
      message: 'Are you sure to delete this tag? You cannot undo this action!',
      header: 'Confirmation',
      icon: 'pi pi-exclamation-triangle',
      accept: () => {
        this.tagService.deleteTag(tagId).subscribe(response => {
          this.tags = this.tags.filter(x => x.getTagId() !== tagId);
        },
          error => {
            this.hasError = true;
            this.errorMessage = error;
          })
      }
    });
  }


}
