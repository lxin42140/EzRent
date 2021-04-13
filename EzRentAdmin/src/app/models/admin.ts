import { UserAccessRightEnum } from './user-access-right-enum.enum';
export class Admin {

    adminId: number | undefined;
	firstName: string | undefined;
	lastName: string | undefined;
	userAccessRightEnum: UserAccessRightEnum | undefined;
	username: string | undefined;
    password: string | undefined;
    email: string | undefined;
    isDeleted : boolean | undefined;
    isDisabled : boolean | undefined;

    constructor(adminId?: number, firstName?: string, lastName?: string, 
        userAccessRightEnum?: UserAccessRightEnum, username?: string, 
        password?: string, email?: string, isDeleted?: boolean, isDisable?: boolean) {
            this.adminId = adminId;
            this.firstName = firstName;
            this.lastName = lastName;
            this.userAccessRightEnum = userAccessRightEnum;
            this.username = username;
            this.password = password;
            this.email = email;
            this.isDeleted = isDeleted;
            this.isDisabled = isDisable;
    }

}
