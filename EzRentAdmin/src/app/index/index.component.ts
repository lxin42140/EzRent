import { SessionService } from './../services/session.service';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-index',
  templateUrl: './index.component.html',
  styleUrls: ['./index.component.css']
})
export class IndexComponent implements OnInit {

  constructor(public sessionService: SessionService) { }

  ngOnInit(): void {
  }

  parentEvent() { }

}
