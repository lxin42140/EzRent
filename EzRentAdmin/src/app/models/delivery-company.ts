import { Delivery } from "./delivery";
import { UserAccessRightEnum } from "./user-access-right-enum.enum";

export class DeliveryCompany {
   
    userId: number | undefined;
    userName: string | undefined;
    email: string | undefined;
    firstName: string | undefined;
    lastName: string | undefined;
    accessRight: UserAccessRightEnum | undefined;
    isDisable: boolean | undefined;
    isDeleted: boolean | undefined;
    password: string | undefined;

    companyName: string | undefined;
    companyUEN: string | undefined;
    companyContactNumber: String | undefined;

    deliveries: Delivery[] | undefined;    

    constructor(userId?: number, userName?: string, email?: string, 
        firstName?: string, lastName?: string, accessRight?: UserAccessRightEnum, 
        isDisable?: boolean, isDeleted?: boolean, password?: string, 
        companyName?: string, companyUEN?: string, companyContactNumber?: String) {
        this.userId = userId;;
        this.userName = userName;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.accessRight = accessRight;
        this.isDisable = isDisable;
        this.isDeleted = isDeleted;
        this.password = password;
        this.companyName = companyName;
        this.companyUEN = companyUEN;
        this.companyContactNumber = companyContactNumber;
    }
}
