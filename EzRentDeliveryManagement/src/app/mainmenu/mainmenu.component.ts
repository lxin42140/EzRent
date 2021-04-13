import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

import { MenuItem } from 'primeng/api';
import { SessionService } from '../services/session.service';

@Component({
  selector: 'app-mainmenu',
  templateUrl: './mainmenu.component.html',
  styleUrls: ['./mainmenu.component.css']
})
export class MainmenuComponent implements OnInit {

  items: MenuItem[];

  constructor(private router: Router, public sessionService: SessionService) {
    this.items = [{

    }];
  }

  ngOnInit(): void {
    if (this.sessionService.getIsLogin()) {
      this.items = [
        {
          label: 'View all transactions',
          items: [{
            label: 'View all transactions',
            icon: 'pi pi-refresh',
            command: () => {
              this.router.navigate(["/viewAllTransactions"]);
            }
          }]
        },
        {
          label: 'Deliveries',
          items: [{
            label: 'Ongoing deliveries',
            icon: 'pi pi-external-link',
            command: () => {
              this.router.navigate(["/viewAllOngoingDeliveries"])
            }
          },
          {
            label: 'Completed deliveries',
            icon: 'pi pi-upload',
            command: () => {
              this.router.navigate(["/viewAllCompletedDeliveries"])
            }
          },
          ],
        },
        {
          label: 'Company profile',
          items: [{
            label: 'Company profile',
            icon: 'pi pi-upload',
            command: () => {
              this.router.navigate(["/profile"])
            }
          }]
        },
        {
          label: 'Welcome ' + this.sessionService.getCurrentDeliveryCompany().userName,
          items: [{
            label: 'Log out',
            icon: 'pi pi-upload',
            command: () => {
              this.logout()
            }
          }]
        },
      ];
    }
  }

  logout(): void {
    window.sessionStorage.clear();
    this.sessionService.setIsLogin(false);
    this.router.navigateByUrl("index");
  }

}
