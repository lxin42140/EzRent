import { TagService } from '../../services/tag.service';
import { Tag } from '../../models/tag';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-view-all-tags',
  templateUrl: './view-all-tags.component.html',
  styleUrls: ['./view-all-tags.component.css']
})
export class ViewAllTagsComponent implements OnInit {

  tags: Tag[];


  constructor(private tagService: TagService) {
    this.tags = new Array();
  }

  ngOnInit(): void {
    this.tagService.getTags().subscribe(
      response => {
        this.tags = response;
      },
      error => {
        console.log('********** ViewAllTags.ts: ' + error);
      }
    );
  }

}
