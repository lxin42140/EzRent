import { Delivery } from './delivery'

export class UpdateDeliveryReq {

    deliveryComment: string;
    deliveryId: number;
    deliveryStatus: string;

    constructor(deliveryComment: string, deliveryId: number, deliveryStatus: string) {
        this.deliveryComment = deliveryComment;
        this.deliveryId = deliveryId;
        this.deliveryStatus = deliveryStatus;
    }

    getDeliveryComment(): string {
        return this.deliveryComment;
    }
    getDeliveryId(): number {
        return this.deliveryId;
    }

    getDeliveryStatus(): string {
        return this.deliveryStatus;
    }

}
