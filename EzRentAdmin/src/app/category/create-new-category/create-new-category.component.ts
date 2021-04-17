import { CreateCategoryWithParentReq } from './../../models/createCategoryWithParentReq';
import { CreateCategoryReq } from './../../models/createCategoryReq';
import { NgForm } from '@angular/forms';
import { CategoryService } from './../../services/category.service';
import { SessionService } from './../../services/session.service';
import { Category } from './../../models/category';
import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-create-new-category',
  templateUrl: './create-new-category.component.html',
  styleUrls: ['./create-new-category.component.css']
})
export class CreateNewCategoryComponent implements OnInit {

  submitted: boolean;
  newCategory: Category;
  createCategoryReq: CreateCategoryReq | undefined;
  createCategoryWithParentReq: CreateCategoryWithParentReq | undefined;
  selectedValue: boolean;
  resultSuccess: boolean;
  resultError: boolean;
  message: string | undefined;
  parentCategoryId: number | undefined;
  categories: Category[];



  constructor(private router: Router,
    private activatedRoute: ActivatedRoute,
    public sessionService: SessionService,
    private categoryService: CategoryService) {
    this.newCategory = new Category("");
    this.submitted = false;
    this.resultSuccess = false;
    this.resultError = false;
    this.selectedValue = false;
    this.categories = new Array();
  }



  ngOnInit() {
    this.categoryService.getCategories().subscribe(
      response => {
        this.categories = response;
      },
      error => {
        console.log('********** CreateNewCategoryComponent.ts: ' + error);
      }
    );
  }



  clear() {
    this.submitted = false;
    this.newCategory = new Category("");
  }



  create(createCategoryForm: NgForm) {
    console.log("CREATE Method", this.newCategory);
    

    if (this.selectedValue == false) {
      this.createCategoryReq = new CreateCategoryReq(this.sessionService.getUsername(), this.sessionService.getPassword(), this.newCategory);
      this.submitted = true;

      if (createCategoryForm.valid) {
        this.categoryService.createNewRootCategory(this.createCategoryReq).subscribe(
          response => {
            let newCategoryId: number = response;
            this.resultSuccess = true;
            this.resultError = false;
            this.message = "New category " + newCategoryId + " created successfully";
          },
          error => {
            this.resultError = true;
            this.resultSuccess = false;
            this.message = "An error has occurred while creating the new product: " + error;

            console.log('********** CreateNewCategoryComponent.ts: ' + error);
          }
        );
      }

    } else {

      this.createCategoryWithParentReq = new CreateCategoryWithParentReq(this.sessionService.getUsername(), this.sessionService.getPassword(), this.newCategory, this.parentCategoryId);
      this.submitted = true;

      if (createCategoryForm.valid) {
        this.categoryService.createNewRootCategoryWithParentCategory(this.createCategoryWithParentReq).subscribe(
          response => {
            let newCategoryId: number = response;
            this.resultSuccess = true;
            this.resultError = false;
            this.message = "New category " + newCategoryId + " created successfully";
          },
          error => {
            this.resultError = true;
            this.resultSuccess = false;
            this.message = "An error has occurred while creating the new product: " + error;

            console.log('********** CreateNewCategoryComponent.ts: ' + error);
          }
        );
      }
    }

  }

  setSelectedValue(selectedValue : boolean) {
    this.selectedValue = selectedValue;
    console.log("SELECTING Method", this.selectedValue);
  }

  parentEvent() { }

}
