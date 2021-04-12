import { Delivery } from './delivery'

export class DeliveryCompany {

    
    companyName : string | undefined;
    companyUEN : string | undefined;  
    companyContactNumber : string | undefined;
    deliveries : Delivery[] | undefined;

    constructor(companyName : string, companyUEN : string, companyContactNumber : string, deliveries : Delivery[]) {
        this.companyName = companyName;
        this.companyUEN = companyUEN;
        this.companyContactNumber = companyContactNumber;
        this.deliveries = deliveries;
    }
}