import { TagService } from '../services/tag.service';
import {
  SessionService
} from '../services/session.service';
import { Tag } from '../models/tag';
import { Component, OnInit } from '@angular/core';
@Component({
  selector: 'app-tag-management',
  templateUrl: './tag-management.component.html',
  styleUrls: ['./tag-management.component.css']
})
export class TagManagementComponent implements OnInit {

  tags: Tag[];

  constructor(private tagService: TagService, private sessionService: SessionService) {
    this.tags = [];
  }

  ngOnInit(): void {


  }

  deleteTag(tagId: number): void {

    this.tagService.deleteTag(this.sessionService.getUsername(),this.sessionService.getPassword(), tagId).subscribe(response => {

    },
      error => {

      })
  }

}
