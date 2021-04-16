import { Component, OnInit } from '@angular/core';
import { Admin } from '../models/admin';
import { AdminService } from '../services/admin.service';
import { SessionService } from '../services/session.service';

import {animate, state, style, transition, trigger} from '@angular/animations';


@Component({
  selector: 'app-view-all-admins',
  templateUrl: './view-all-admins.component.html',
  styleUrls: ['./view-all-admins.component.css'],
  animations: [
    trigger('detailExpand', [
      state('collapsed', style({height: '0px', minHeight: '0'})),
      state('expanded', style({height: '*'})),
      transition('expanded <=> collapsed', animate('225ms cubic-bezier(0.4, 0.0, 0.2, 1)')),
    ]),
  ],
})
export class ViewAllAdminsComponent implements OnInit {

  displayedHeaders : string[] = ['Id', 'Username', 'First Name', 'Last Name', 'Email'];

  admins: Admin[];

  
  expandedElement : Admin | undefined;

  successMessage: string | undefined;
  errorMessage: string | undefined;

  constructor(private adminService : AdminService,
    public sessionService: SessionService) {
    this.admins = new Array();
  }

  ngOnInit(): void {

    this.adminService.retrieveAllAdmin(this.sessionService.getUsername(), this.sessionService.getPassword()).subscribe(
			response => {
				this.admins = response;
			},
			error => {
				console.log('********** ViewAllAdminComponent.ts: ' + error);
			}
		);

  }

  handleDisableClick(event: Event, admin: Admin): void {
    if (admin.userId !== undefined) {
      this.adminService.updateAdminAccountStatus(this.sessionService.getUsername(), this.sessionService.getPassword(), admin.userId, true).subscribe(
        response => {
          this.successMessage = "Admin (id: " + response.userId + ") has been disabled!";
          this.ngOnInit();
        }, error => {
          this.errorMessage = error;
        }
      )
    }
  }

  handleEnableClick(event: Event, admin: Admin): void {
    if (admin.userId !== undefined) {
      this.adminService.updateAdminAccountStatus(this.sessionService.getUsername(), this.sessionService.getPassword(), admin.userId, false).subscribe(
        response => {
          this.successMessage = "Delivery company (id: " + response.userId + ") has been enabled!";
          this.ngOnInit();
        }, error => {
          this.errorMessage = error;
        }
      )
    }
  }

}
