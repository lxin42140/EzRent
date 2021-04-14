import { Component, OnInit } from '@angular/core';
import { Admin } from '../models/admin';
import { AdminService } from '../services/admin.service';
import { SessionService } from '../services/session.service';

@Component({
  selector: 'app-view-all-admins',
  templateUrl: './view-all-admins.component.html',
  styleUrls: ['./view-all-admins.component.css']
})
export class ViewAllAdminsComponent implements OnInit {

  admins: Admin[];

  parentEvent() { }

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

}
