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

  createAdminSuccess: boolean;
	createAdminError: boolean;

  // emailFormControl = new FormControl('', [
  //   Validators.required,
  //   Validators.email,
  // ]);

  // nameFormControl = new FormControl('', [
  //   Validators.required,
  //   Validators.maxLength(32)
  // ]);

  constructor(private adminService : AdminService, 
		public sessionService: SessionService) {
    this.admin = new Admin();
    this.createAdminSuccess = false;
		this.createAdminError = false;
  }

  ngOnInit(): void {
  }

  createAdmin(createAdminForm: NgForm) {
        
    if (createAdminForm.valid) 
		{
      var createAdminReq = new CreateAdminReq(this.sessionService.getUsername(), this.sessionService.getPassword(), this.admin);
      console.log(this.sessionService.getUsername() + "       " +  this.sessionService.getPassword());
			this.adminService.createNewAdmin(createAdminReq).subscribe(
				response => {
					//let newAdminId: number = response;
					this.createAdminSuccess = true;
          this.createAdminError = false;
					this.message = "New Admin User (id: " + response.userId + ") created successfully";
				},
				error => {
					this.createAdminError = true;
          this.createAdminSuccess = false;
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
