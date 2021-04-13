import { Admin } from './../models/admin';
import { Injectable } from '@angular/core';

import { UserAccessRightEnum } from '../models/user-access-right-enum.enum';

@Injectable({
  providedIn: 'root'
})
export class SessionService {
  constructor() {

  }



  getIsLogin(): boolean {
    if (sessionStorage.isLogin == "true") {
      return true;
    }
    else {
      return false;
    }
  }



  setIsLogin(isLogin: boolean): void {
    sessionStorage.isLogin = isLogin;
  }



  getCurrentAdmin(): Admin {
    return JSON.parse(sessionStorage.currentAdmin);
  }



  setCurrentAdmin(currentAdmin: Admin | null): void {
    sessionStorage.currentAdmin = JSON.stringify(currentAdmin);
  }



  getUsername(): string {
    return sessionStorage.username;
  }



  setUsername(username: string | undefined): void {
    sessionStorage.username = username;
  }



  getPassword(): string {
    return sessionStorage.password;
  }



  setPassword(password: string | undefined): void {
    sessionStorage.password = password;
  }



  checkAccessRight(path: string): boolean {
    if (this.getIsLogin()) {
      let admin: Admin = this.getCurrentAdmin();

      if (admin.userAccessRightEnum == UserAccessRightEnum.ADMINSTRATOR) {
        return true;
      }
      else {
        return false;
      }
    }
    return true;
  }
}
