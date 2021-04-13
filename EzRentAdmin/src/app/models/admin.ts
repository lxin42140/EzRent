import { UserAccessRightEnum } from './user-access-right-enum.enum';
export class Admin {

    userId: number | undefined;
	firstName: string | undefined;
	lastName: string | undefined;
	userAccessRightEnum: string | undefined;
	userName: string | undefined;
    password: string | undefined;
    email: string | undefined;
    isDeleted : boolean | undefined;
    isDisabled : boolean | undefined;

    constructor(userId?: number, firstName?: string, lastName?: string, 
        userAccessRightEnum?: string, userName?: string, 
        password?: string, email?: string, isDeleted?: boolean, isDisable?: boolean) {
            this.userId = userId;
            this.firstName = firstName;
            this.lastName = lastName;
            this.userAccessRightEnum = userAccessRightEnum;
            this.userName = userName;
            this.password = password;
            this.email = email;
            this.isDeleted = isDeleted;
            this.isDisabled = isDisable;
    }

}
