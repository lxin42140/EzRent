import { Admin } from './admin';
export class CreateAdminReq {
    username: string;
    password: string;
    newAdmin: Admin;

    constructor(username: string, password: string, newAdmin: Admin) {
        this.username = username;
        this.password = password;
        this.newAdmin = newAdmin;
    }

    getUsername(): string {
        return this.username;
    }

    getPassword(): string {
        return this.password;
    }

    getNewAdmin(): Admin {
        return this.newAdmin;
    }
}
