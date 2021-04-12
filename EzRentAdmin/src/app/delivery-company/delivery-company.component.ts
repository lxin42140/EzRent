import { Component, OnInit } from '@angular/core';
import { DeliveryCompany } from '../models/delivery-company';
import { DeliveryCompanyService } from '../services/delivery-company.service';

@Component({
  selector: 'app-delivery-company',
  templateUrl: './delivery-company.component.html',
  styleUrls: ['./delivery-company.component.css']
})
export class DeliveryCompanyComponent implements OnInit {

  message: string | undefined;
  deliveryCompany: DeliveryCompany;

  constructor(private deliveryCompanyService: DeliveryCompanyService) {
    this.deliveryCompany = new DeliveryCompany();
  }

  ngOnInit(): void {
  }

  createDeliveryCompany() {
    this.deliveryCompanyService.createNewDeliveryCompany(this.deliveryCompany).subscribe(
      response => {
				let newDeliveryCompanyId: number = response;				
				this.message = "New DeliveryCompany " + newDeliveryCompanyId + " User created successfully";
			},
			error => {				
				this.message = "An error has occurred while creating the new delivery company: " + error;
				
				console.log('********** CreateNewRecordComponent.ts: ' + error);    
      }
    );    
  }

}
