import { Component, OnInit } from '@angular/core';

import { ActivatedRoute } from '@angular/router';
import { NgForm } from '@angular/forms';

import { ConfirmationService } from 'primeng/api';

import { DeliveryService } from '../services/delivery.service'
import { Delivery } from '../models/delivery'
import { SessionService } from '../services/session.service';
import { UpdateDeliveryReq } from '../models/update-delivery-req';

@Component({
  selector: 'app-view-all-ongoing-deliveries',
  templateUrl: './view-all-ongoing-deliveries.component.html',
  styleUrls: ['./view-all-ongoing-deliveries.component.css'],
  providers: [ConfirmationService]
})

export class ViewAllOngoingDeliveriesComponent implements OnInit {


  ongoingDeliveries: Delivery[];
  selectedDelivery: Delivery | undefined;
  updatedDelivery: UpdateDeliveryReq | undefined;

  deliveryComment: string;
  successMessage: string | undefined;

  shippedDialog: boolean;
  deliveredDialog: boolean;
  lostDialog: boolean;

  hasError: boolean;
  errorMessage: string | undefined;

  constructor(private activatedRoute: ActivatedRoute,
    private deliveryService: DeliveryService,
    private sessionService: SessionService,
    private confirmationService: ConfirmationService
  ) {

    this.ongoingDeliveries = new Array();
    this.deliveryComment = "";

    this.shippedDialog = false;
    this.deliveredDialog = false;
    this.lostDialog = false;
    this.hasError = true;
  }

  ngOnInit(): void {

    // if (this.sessionService.getIsLogin()) {
    this.deliveryService.getDeliveries().subscribe(
      response => {
        this.ongoingDeliveries = response.filter(x => x.deliveryStatus != "DELIVERED");
      },
      error => {
        console.log("********* View All Ongoing Deliveries: error at ngOnInit: " + error);
      }
    );
    // }
  }

  handleClick(event: Event, delivery: Delivery, status: string): void {
    this.selectedDelivery = delivery;

    if (this.selectedDelivery.deliveryComment == null || this.selectedDelivery.deliveryComment == undefined) {
      this.deliveryComment = "";
    } else {
      this.deliveryComment = this.selectedDelivery.deliveryComment;
    }

    switch (status) {
      case ('SHIPPED'):
        this.shippedDialog = true;
        break;
      case ('DELIVERED'):
        this.deliveredDialog = true;
        break;
      case ('LOST'):
        this.lostDialog = true;
        break;
      default:
        break;
    }
  }


  confirmShipped(): void {
    this.confirmationService.confirm({
      message: 'Are you sure that this delivery is shipped? You cannot undo this action!',
      header: 'Confirmation',
      icon: 'pi pi-exclamation-triangle',
      accept: () => {
        //Actual logic to perform a confirmation
        if (this.selectedDelivery !== undefined) {
          // if (this.deliveryComment.length == 0) {
          //   this.selectedDelivery.deliveryComment = undefined;
          // }

          //NEED TO CHANGE SERVICE METHOD!! TO UPDATE THE COMMENT
          // this.deliveryService.updateDelivery(this.selectedDelivery.deliveryId, "SHIPPED");

          this.updatedDelivery = new UpdateDeliveryReq(this.deliveryComment, this.selectedDelivery.deliveryId, "SHIPPED");
          this.deliveryService.updateDelivery(this.updatedDelivery).subscribe(
            response => {
              this.successMessage = response;
            },
            error => {
              this.hasError = true;
              this.errorMessage = error;
            }
          )
          // .subscribe(
          // response => {
          //   this.successMessage = "Delivery status successfully changed to SHIPPED";
          //   this.shippedDialog = false;
          // }, error => {
          //   this.errorMessage = error;
          //   this.shippedDialog = false;
          // }
          // );    
        }
      }
    });
  }

  confirmDelivered(): void {
    this.confirmationService.confirm({
      message: 'Are you sure that this delivery is shipped? You cannot undo this action!',
      header: 'Confirmation',
      icon: 'pi pi-exclamation-triangle',
      accept: () => {
        //Actual logic to perform a confirmation
        if (this.selectedDelivery != null) {
          // if (this.deliveryComment.length == 0) {
          //   this.selectedDelivery.deliveryComment = undefined;
          // }

          //NEED TO CHANGE SERVICE METHOD!! TO UPDATE THE COMMENT
          // this.deliveryService.updateDelivery(this.selectedDelivery.deliveryId, "DELIVERED");

          // constructor(deliveryComment: string, deliveryId: number, deliveryStatus: string) {
          //   this.deliveryComment = deliveryComment;
          //   this.deliveryId = deliveryId;
          //   this.deliveryStatus = deliveryStatus;
          // }

          this.updatedDelivery = new UpdateDeliveryReq(this.deliveryComment, this.selectedDelivery.deliveryId, "DELIVERED");
          this.deliveryService.updateDelivery(this.updatedDelivery);

          this.shippedDialog = false;
        }

        this.deliveredDialog = false;
      }
    });
  }

  confirmLost(): void {
    this.confirmationService.confirm({
      message: 'Are you sure that this delivery is shipped? You cannot undo this action!',
      header: 'Confirmation',
      icon: 'pi pi-exclamation-triangle',
      accept: () => {
        //Actual logic to perform a confirmation
        if (this.selectedDelivery != null) {
          // if (this.deliveryComment.length == 0) {
          //   this.selectedDelivery.deliveryComment = undefined;
          // }

          //NEED TO CHANGE SERVICE METHOD!! TO UPDATE THE COMMENT
          // this.deliveryService.updateDelivery(this.selectedDelivery.deliveryId, "LOST");

          this.updatedDelivery = new UpdateDeliveryReq(this.deliveryComment, this.selectedDelivery.deliveryId, "LOST");
          this.deliveryService.updateDelivery(this.updatedDelivery);

          //         this.shippedDialog = false;
        }


        //       this.lostDialog = false;
      }
    });
  }



}