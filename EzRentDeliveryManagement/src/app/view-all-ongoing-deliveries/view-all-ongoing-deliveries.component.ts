import { Component, OnInit } from '@angular/core';

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

  constructor(private deliveryService: DeliveryService,
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

    if (this.sessionService.getIsLogin()) {
      this.deliveryService.getDeliveries().subscribe(
        response => {
          this.ongoingDeliveries = response.filter(x => x.deliveryStatus != "DELIVERED" && x.deliveryStatus != "LOST");
        },
        error => {
          console.log("********* View All Ongoing Deliveries: error at ngOnInit: " + error);
        }
      );
    }
  }

  handleClick(event: Event, delivery: Delivery, status: string): void {
    this.selectedDelivery = delivery;
    console.log("last update date: " + delivery.lastUpdateDate);

    this.hasError = false;
    this.errorMessage = "";

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
        if (this.selectedDelivery !== undefined) {
          let deliveryId = this.selectedDelivery.deliveryId;
          this.updatedDelivery = new UpdateDeliveryReq(this.deliveryComment, deliveryId, "SHIPPED");
          this.deliveryService.updateDelivery(this.updatedDelivery).subscribe(
            response => {
              this.successMessage = response;
              this.shippedDialog = false;
              this.ongoingDeliveries = this.ongoingDeliveries.map(x => {
                if (x.deliveryId == deliveryId) {
                  x.deliveryStatus = "SHIPPED";
                  return x;
                } else {
                  return x;
                }
              });
            },
            error => {
              this.hasError = true;
              this.errorMessage = error;
            }
          )
        }
      }
    });
  }

  confirmDelivered(): void {
    this.confirmationService.confirm({
      message: 'Are you sure that this delivery is delivered? You cannot undo this action!',
      header: 'Confirmation',
      icon: 'pi pi-exclamation-triangle',
      accept: () => {
        if (this.selectedDelivery != null) {
          let deliveryId = this.selectedDelivery.deliveryId;
          this.updatedDelivery = new UpdateDeliveryReq(this.deliveryComment, deliveryId, "DELIVERED");
          this.deliveryService.updateDelivery(this.updatedDelivery).subscribe(
            response => {
              this.successMessage = response;
              this.deliveredDialog = false;
              this.ongoingDeliveries = this.ongoingDeliveries.filter(x => x.deliveryId != deliveryId);
            },
            error => {
              this.hasError = true;
              this.errorMessage = error;
            }
          )
        }
      }
    });
  }

  confirmLost(): void {
    this.confirmationService.confirm({
      message: 'Are you sure that this delivery is lost? You cannot undo this action!',
      header: 'Confirmation',
      icon: 'pi pi-exclamation-triangle',
      accept: () => {
        if (this.selectedDelivery != null) {
          let deliveryId = this.selectedDelivery.deliveryId;
          this.updatedDelivery = new UpdateDeliveryReq(this.deliveryComment, deliveryId, "LOST");
          this.deliveryService.updateDelivery(this.updatedDelivery).subscribe(
            response => {
              this.successMessage = response;
              this.lostDialog = false;
              this.ongoingDeliveries = this.ongoingDeliveries.filter(x => x.deliveryId != deliveryId);
            },
            error => {
              this.hasError = true;
              this.errorMessage = error;
            }
          )

        }
      }
    });
  }



}
