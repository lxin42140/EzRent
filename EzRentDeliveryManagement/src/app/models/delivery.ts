import { DeliveryStatusEnum } from './delivery-status-enum'
import { DeliveryCompany } from './delivery-company'
import { Transaction } from './transaction'

export class Delivery {

    deliveryId: number | undefined;
    deliveryStatus : DeliveryStatusEnum | undefined;
    deliveryComment : string | undefined;
    lastUpdateDate : Date | undefined;
    deliveryCompany : DeliveryCompany | undefined;
    transaction : Transaction | undefined;

    constructor(deliveryId: number, deliveryStatus : DeliveryStatusEnum, deliveryComment : string, lastUpdateDate : Date, deliveryCompany : DeliveryCompany, transaction : Transaction) {
        this.deliveryId = deliveryId;
        this.deliveryStatus = deliveryStatus;
        this.deliveryComment = deliveryComment;
        this.lastUpdateDate = lastUpdateDate;
        this.deliveryCompany = deliveryCompany;
        this.transaction = transaction;
    }

}
