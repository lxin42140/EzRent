import { Delivery } from './delivery'

export class DeliveryCompany {

    userId: Number;
    userName: String;
    password: String | undefined;
    email: String;
    firstName: String;
    lastName: String;
    companyName: string;
    companyUEN: string;
    companyContactNumber: string;
    deliveries: Delivery[];

    constructor(
        userId: Number,
        userName: String,
        password: String | undefined,
        email: String,
        firstName: String,
        lastName: String,
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