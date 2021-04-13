import { DeliveryCompanyService } from '../services/delivery-company-service.service';
import { SessionService } from '../services/session.service';
import { Router } from '@angular/router';
import { Component, OnInit } from '@angular/core';
@Component({
  selector: 'app-index',
  templateUrl: './index.component.html',
  styleUrls: ['./index.component.css']
})
export class IndexComponent implements OnInit {

  username: string;
  password: string; 
  loginError: boolean;
  errorMessage: string | undefined;

  constructor(private deliveryCompanyService: DeliveryCompanyService, private sessionService: SessionService, private route: Router) { 
    this.username = "";
    this.password = "";
    this.loginError = false;
  }

  ngOnInit(): void {
  }

  login(): void {
    this.deliveryCompanyService.deliveryCompanyLogin(this.username, this.password).subscribe(
      response => {
        this.sessionService.setIsLogin(true);
        this.sessionService.setDeliveryCompany(response);
        this.route.navigateByUrl('/viewAllTransactions');
      },
      error => {
        this.loginError = true;
        this.errorMessage = error.split(':')[3];
      }
    )
  }

}
