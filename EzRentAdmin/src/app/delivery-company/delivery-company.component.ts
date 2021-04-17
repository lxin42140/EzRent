import { Component, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';

import { DeliveryCompany } from '../models/delivery-company';
import { DeliveryCompanyService } from '../services/delivery-company.service';
import { UserAccessRightEnum } from '../models/user-access-right-enum.enum'
import { CreateDeliveryCompanyReq } from '../models/CreateDeliveryCompanyReq';
import { SessionService } from '../services/session.service';

@Component({
  selector: 'app-delivery-company',
  templateUrl: './delivery-company.component.html',
  styleUrls: ['./delivery-company.component.css']
})
export class DeliveryCompanyComponent implements OnInit {

  message: string | undefined;
  createCompanySuccess : boolean;
  createCompanyError : boolean;
  deliveryCompany: DeliveryCompany;
  createDeliveryCompanyReq: CreateDeliveryCompanyReq | undefined;

  constructor(private deliveryCompanyService: DeliveryCompanyService,
    private sessionService: SessionService) {
    this.deliveryCompany = new DeliveryCompany();
    this.createCompanySuccess = false;
    this.createCompanyError = false;
  }

  ngOnInit(): void {
  }

  createDeliveryCompany(createDeliveryCompanyForm: NgForm) {

    this.deliveryCompany.isDisable = false;
    this.deliveryCompany.isDeleted = false;
    // this.deliveryCompany.accessRight = UserAccessRightEnum.DELIVERY_COMPANY;

    let username = this.sessionService.getUsername();
    let password = this.sessionService.getPassword();

    this.createDeliveryCompanyReq = new CreateDeliveryCompanyReq(username, password, this.deliveryCompany);

    if (this.createDeliveryCompanyReq !== undefined) {
      this.deliveryCompanyService.createNewDeliveryCompany(this.createDeliveryCompanyReq).subscribe(
        response => {
          // let newDeliveryCompanyId: number = response;				
          this.message = "New DeliveryCompany (id: " + response.userId + ") created successfully";
          this.createCompanySuccess = true;
          this.createCompanyError = false;
        },
        error => {
          this.message = "An error has occurred while creating the new delivery company: " + error;
          this.createCompanyError = true;
          this.createCompanySuccess = false;
          console.log('********** createDeliveryCompany delivery-company.component.ts: ' + error);
        }
      );
    }
  }

}
