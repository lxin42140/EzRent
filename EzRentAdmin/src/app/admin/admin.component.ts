import { Component, OnInit } from '@angular/core';
import { FormControl, NgForm, Validators, FormsModule } from '@angular/forms';
import { Admin } from '../models/admin';
import { CreateAdminReq } from '../models/create-admin-req';
import { AdminService } from '../services/admin.service';
import { SessionService } from '../services/session.service';

@Component({
  selector: 'app-admin',
  templateUrl: './admin.component.html',
  styleUrls: ['./admin.component.css']
})
export class AdminComponent implements OnInit {

  message: string | undefined;
  admin : Admin;

  resultSuccess: boolean;
	resultError: boolean;

  emailFormControl = new FormControl('', [
    Validators.required,
    Validators.email,
  ]);

  nameFormControl = new FormControl('', [
    Validators.required,
    Validators.maxLength(32)
  ]);

  constructor(private adminService : AdminService, 
		public sessionService: SessionService) {
    this.admin = new Admin();
    this.resultSuccess = false;
		this.resultError = false;
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
    if (createAdminForm.valid) 
		{
      var createAdminReq = new CreateAdminReq(this.sessionService.getUsername(), this.sessionService.getPassword(), this.admin);
      console.log(this.sessionService.getUsername() + "       " +  this.sessionService.getPassword());
			this.adminService.createNewAdmin(createAdminReq).subscribe(
				response => {
					let newAdminId: number = response;
					this.resultSuccess = true;
					this.resultError = false;
					this.message = "New Admin User " + newAdminId + " created successfully";
				},
				error => {
					this.resultError = true;
					this.resultSuccess = false;
					this.message = "An error has occurred while creating the new admin: " + error;
					
					console.log('********** create admin AdminComponent.ts: ' + error);
				}
			);
		}
    else 
    {
      console.log("@@@form not valid@@@");

    }
  }

}
