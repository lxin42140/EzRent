import { Delivery } from './delivery'

export class DeliveryCompany {

    userId: number;
    userName: string;
    password: string | undefined;
    email: string;
    firstName: string;
    lastName: string;
    companyName: string;
    companyUEN: string;
    companyContactNumber: string;
    deliveries: Delivery[];

    constructor(
        userId: number,
        userName: string,
        password: string | undefined,
        email: string,
        firstName: string,
        lastName: string,
        companyName: string,
        companyUEN: string,
        companyContactNumber: string,
        deliveries: Delivery[]
    ) {
        this.userId = userId;
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.companyName = companyName;
        this.companyContactNumber = companyContactNumber;
        this.companyUEN = companyUEN;
        this.deliveries = deliveries;
    }

}