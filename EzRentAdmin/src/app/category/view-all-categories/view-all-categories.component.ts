import { CategoryService } from '../../services/category.service';
import { Category } from '../../models/category';
import { Component, OnInit } from '@angular/core';
import {TreeNode} from 'primeng/api';
import { Router } from '@angular/router';
import {ConfirmationService} from 'primeng/api';
import { PrimeNGConfig } from 'primeng/api';

@Component({
  selector: 'app-view-all-categories',
  templateUrl: './view-all-categories.component.html',
  styleUrls: ['./view-all-categories.component.css'],
  providers: [ConfirmationService]
})
export class ViewAllCategoriesComponent implements OnInit {
  categories: Category[];
  message: string | undefined;
  resultSuccess: boolean;
  resultError: boolean;


  constructor(private categoryService: CategoryService, private router: Router, private confirmationService: ConfirmationService, private primengConfig: PrimeNGConfig) {
    this.categories = new Array();
    this.message = "";
    this.resultError = false;
    this.resultSuccess = false;
    
  }



  ngOnInit(): void {
    this.primengConfig.ripple = true;
    this.categoryService.getCategories().subscribe(
      response => {
        this.categories = response;
      },
      error => {
        console.log('********** ViewAllCategoriesComponent.ts: ' + error);
      }
    );
  }

  deleteCategory(categoryId: number) {
			this.categoryService.deleteCategory(categoryId).subscribe(
				response => {
          this.router.navigate(["/category/viewAllCategories"]);
          this.ngOnInit();
          this.resultSuccess = true;
          this.resultError = false;
          this.message = "Category successfully deleted!";
          
				},
				error => {
          
          this.resultSuccess = false;
          this.resultError = true;
					this.message = "An error has occurred while deleting this category:" + error;
				}
			);
    }
    
    confirm(categoryId: number) {
      this.confirmationService.confirm({
          message: 'Do you want to delete this category?',
          header: 'Delete Confirmation',
          icon: 'pi pi-exclamation-triangle',
          accept: () => {
            this.deleteCategory(categoryId);
          },
          reject: () => {
          }
      });
  }
}