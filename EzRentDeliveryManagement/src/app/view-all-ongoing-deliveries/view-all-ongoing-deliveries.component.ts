import { Component, OnInit } from '@angular/core';

import { ActivatedRoute } from '@angular/router';
import { NgForm } from '@angular/forms';

import { ConfirmationService } from 'primeng/api';

import { DeliveryService } from '../services/delivery.service'
import { Delivery } from '../models/delivery'

@Component({
  selector: 'app-view-all-ongoing-deliveries',
  templateUrl: './view-all-ongoing-deliveries.component.html',
  styleUrls: ['./view-all-ongoing-deliveries.component.css']
})

export class ViewAllOngoingDeliveriesComponent implements OnInit {


  ongoingDeliveries: Delivery[];
  selectedDelivery: Delivery | undefined;

  deliveryComment : string;

  shippedDialog: boolean;
  deliveredDialog: boolean;
  lostDialog: boolean;

  constructor(private activatedRoute: ActivatedRoute,
    private deliveryService: DeliveryService,
    private confirmationService: ConfirmationService) {

    this.ongoingDeliveries = new Array();
    this.deliveryComment = "";

    this.shippedDialog = false;
    this.deliveredDialog = false;
    this.lostDialog = false;
  }

  ngOnInit(): void {
    this.deliveryService.getDeliveries().subscribe(
      response => {
        response.map(x => x.deliveryId == sessionStorage.getDeliveryCompany().deliveryCompanyId);
        this.ongoingDeliveries = response;
      },
      error => {
        console.log("********* View All Ongoing Deliveries: error at ngOnInit: " + error);
      }
    )
  }

  showShippedDialog(delivery: Delivery): void {
    this.shippedDialog = true;
    this.selectedDelivery = delivery;
    // if (this.selectedDelivery.deliveryComment == null) {
    //   this.selectedDelivery.deliveryComment = "";
    // }
  }

  confirmShipped(): void {
    this.confirmationService.confirm({
      message: 'Are you sure that this delivery is shipped? You cannot undo this action!',
      header: 'Confirmation',
      icon: 'pi pi-exclamation-triangle',
      accept: () => {
        //Actual logic to perform a confirmation


        this.shippedDialog = false;
      }
    });
  }

  showDeliveredDialog(delivery: Delivery): void {
    this.deliveredDialog = true;
    this.selectedDelivery = delivery;
    if (this.selectedDelivery.deliveryComment == null) {
      this.selectedDelivery.deliveryComment = "";
    }
  }

  confirmDelivered(): void {
    this.confirmationService.confirm({
      message: 'Are you sure that this delivery is shipped? You cannot undo this action!',
      header: 'Confirmation',
      icon: 'pi pi-exclamation-triangle',
      accept: () => {
        //Actual logic to perform a confirmation


        this.shippedDialog = false;
      }
    });
  }

  showLostDialog(delivery: Delivery): void {
    this.lostDialog = true;
    this.selectedDelivery = delivery;
    if (this.selectedDelivery.deliveryComment == null) {
      this.selectedDelivery.deliveryComment = "";
    }
  }

  confirmLost(): void {
    this.confirmationService.confirm({
      message: 'Are you sure that this delivery is shipped? You cannot undo this action!',
      header: 'Confirmation',
      icon: 'pi pi-exclamation-triangle',
      accept: () => {
        //Actual logic to perform a confirmation


        this.shippedDialog = false;
      }
    });
  }

  

}
