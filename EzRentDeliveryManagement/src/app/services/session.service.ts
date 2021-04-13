import { Injectable } from '@angular/core';

import { DeliveryCompany } from '../models/delivery-company';

@Injectable({
  providedIn: 'root'
})
export class SessionService {
  constructor() {
    sessionStorage.isLogin = false;
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

  getCurrentDeliveryCompany(): DeliveryCompany {
    return JSON.parse(sessionStorage.deliveryCompany);
  }

  setDeliveryCompany(deliveryCompany: DeliveryCompany | null): void {
    sessionStorage.deliveryCompany = JSON.stringify(deliveryCompany);
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

}
