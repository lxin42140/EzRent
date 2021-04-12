import { DeliveryStatusEnum } from './delivery-status-enum'

export class Delivery {

    deliveryId: number | undefined;
    deliveryStatus : DeliveryStatusEnum | undefined;
    deliveryComment: string | undefined;
    lastUpdateDate : Date | undefined;

    constructor(deliveryStatus : DeliveryStatusEnum, deliveryComment: string, lastUpdateDate : Date) {
        this.deliveryStatus = deliveryStatus;
        this.deliveryComment = deliveryComment;
        this.lastUpdateDate = lastUpdateDate;
    }

}
