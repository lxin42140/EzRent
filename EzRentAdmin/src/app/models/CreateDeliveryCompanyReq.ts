import { DeliveryCompany } from './delivery-company';
export class CreateDeliveryCompanyReq {
    username: string;
    password: string;
    newDeliveryCompany: DeliveryCompany;

    constructor(username: string, password: string, newDeliveryCompany: DeliveryCompany) {
        this.username = username;
        this.password = password;
        this.newDeliveryCompany = newDeliveryCompany;
    }

    getUsername(): string {
        return this.username;
    }

    getPassword(): string {
        return this.password;
    }

    getNewDeliveryCompany(): DeliveryCompany {
        return this.newDeliveryCompany;
    }

}