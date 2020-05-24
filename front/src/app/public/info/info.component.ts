import { Component, OnInit } from '@angular/core';
import { PublicService } from '../../service/public.service';

@Component({
  selector: 'app-info',
  templateUrl: './info.component.html',
  styleUrls: ['./info.component.scss']
})
export class InfoComponent implements OnInit {
  info;
  constructor(private publicService: PublicService) { }

  ngOnInit() {
    console.log(localStorage.getItem('idUnique'));
    this.info = this.publicService.getInfo(localStorage.getItem('idUnique'));
  }

}
