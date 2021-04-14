import { Component, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { Admin } from '../models/admin';
import { AdminService } from '../services/admin.service';

@Component({
  selector: 'app-admin',
  templateUrl: './admin.component.html',
  styleUrls: ['./admin.component.css']
})
export class AdminComponent implements OnInit {

  message: string | undefined;
  // admin : Admin;

  constructor(private adminService : AdminService) {
    // this.admin = new Admin();
  }

  ngOnInit(): void {
  }

  createAdmin(createAdminForm: NgForm) {
    // this.adminService.createNewAdmin(this.admin).subscribe(
    //   response => {
		// 		let newAdminId: number = response;				
		// 		this.message = "New Admin " + newAdminId + " User created successfully";
		// 	},
		// 	error => {				
		// 		this.message = "An error has occurred while creating the new admin: " + error;
				
		// 		console.log('********** createAdmin admin.component.ts: ' + error);    
    //   }
    // );    
  }

}
