import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

import {MenuItem} from 'primeng/api';

@Component({
  selector: 'app-mainmenu',
  templateUrl: './mainmenu.component.html',
  styleUrls: ['./mainmenu.component.css']
})
export class MainmenuComponent implements OnInit {

  items : MenuItem[];

  constructor(private router: Router) { 
    this.items = [{
      label: 'Transactions',
      items: [{
          label: 'View all transactions',
          icon: 'pi pi-refresh',
          command: () => {
            this.router.navigate(["/viewAllTransactions"]);
          }
      }
      ]},
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
          {
            label: 'Lost deliveries',
            icon: 'pi pi-upload',
            command: () => {
              this.router.navigate(["/viewAllLostDeliveries"])
            }
        }
      ]}
  ];
  }

  ngOnInit(): void {
  }

}
