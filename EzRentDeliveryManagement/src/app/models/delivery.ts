export class Delivery {

    deliveryId: number | undefined;
    deliveryStatus : string;
    deliveryComment : string;
    lastUpdateDate : Date;

    constructor(deliveryStatus : string, deliveryComment : string, lastUpdateDate : Date) {
        this.deliveryStatus = deliveryStatus;
        this.deliveryComment = deliveryComment;
        this.lastUpdateDate = lastUpdateDate;
    }

}
