import { CategoryService } from '../../services/category.service';
import { Category } from '../../models/category';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-view-all-categories',
  templateUrl: './view-all-categories.component.html',
  styleUrls: ['./view-all-categories.component.css']
})
export class ViewAllCategoriesComponent implements OnInit {
  records: Category[] | null;



  constructor(private categoryService: CategoryService) {
    this.records = new Array();
  }



  ngOnInit(): void {
    this.categoryService.getCategories().subscribe(
      response => {
        this.records = response;
      },
      error => {
        console.log('********** ViewAllCategoriesComponent.ts: ' + error);
      }
    );
  }
}