export class Delivery {

    deliveryId: number | undefined;
    deliveryStatus : string | undefined;
    deliveryComment : string | undefined;
    lastUpdateDate : Date | undefined;

    constructor(deliveryStatus : string, deliveryComment : string, lastUpdateDate : Date) {
        this.deliveryStatus = deliveryStatus;
        this.deliveryComment = deliveryComment;
        this.lastUpdateDate = lastUpdateDate;
    }

}
