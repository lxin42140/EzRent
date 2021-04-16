import { NgForm } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { SessionService } from '../../services/session.service';
import { CategoryService } from '../../services/category.service';
import { Category } from '../../models/category';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-update-category-name',
  templateUrl: './update-category-name.component.html',
  styleUrls: ['./update-category-name.component.css']
})
export class UpdateCategoryNameComponent implements OnInit {

  submitted: boolean;
  categoryId: string;
  categoryToUpdate: Category;
  categoryName: string;
  retrieveCategoryError: boolean;

  resultSuccess: boolean;
  resultError: boolean;
  message: string | undefined;

  constructor(private router: Router,
    private activatedRoute: ActivatedRoute,
    public sessionService: SessionService,
    private categoryService: CategoryService) {
    this.categoryId = "";
    this.categoryToUpdate = new Category("");
    this.categoryName = "";
    this.submitted = false;
    this.retrieveCategoryError = false;

    this.resultSuccess = false;
    this.resultError = false;
  }



  ngOnInit() {
    this.categoryId = String(this.activatedRoute.snapshot.paramMap.get("categoryId"));
    
    if (this.categoryId != null) {

      this.categoryService.getCategoryById(parseInt(this.categoryId)).subscribe(
        response => {
        },
        error => {
          this.retrieveCategoryError = true;
          console.log('********** UpdateCategoryComponent.ts: ' + error);
        }
      );

    }
  }



  update(updateCategoryForm: NgForm) {

    this.submitted = true;
    if (updateCategoryForm.valid) {
      this.categoryService.updateCategory(parseInt(this.categoryId), this.categoryName).subscribe(
        response => {
          this.resultSuccess = true;
          this.resultError = false;
          this.message = "Category name updated successfully";
        },
        error => {
          this.resultError = true;
          this.resultSuccess = false;
          this.message = "An error has occurred while updating the category name: " + error;

          console.log('********** UpdateCategoryComponent.ts: ' + error);
        }
      );
    }
  }

  clear() {
    this.categoryName = "";

  }

  parentEvent() {
    
  }

}
