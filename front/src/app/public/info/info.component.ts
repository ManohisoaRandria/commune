import { Component, OnInit } from '@angular/core';
import { PublicService } from '../../service/public.service';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-info',
  templateUrl: './info.component.html',
  styleUrls: ['./info.component.scss']
})
export class InfoComponent implements OnInit {
  info;
  constructor(private publicService: PublicService, private httpClient: HttpClient) { }

  ngOnInit() {
    this.publicService.getInfo(localStorage.getItem('idUnique')).then((res) => {
      this.info = res;
    });
  }

}
