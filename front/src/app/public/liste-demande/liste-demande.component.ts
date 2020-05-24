import { Component, OnInit } from '@angular/core';
import { PublicService } from '../../service/public.service';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-liste-demande',
  templateUrl: './liste-demande.component.html',
  styleUrls: ['./liste-demande.component.scss']
})
export class ListeDemandeComponent implements OnInit {

  constructor(private publicService: PublicService, private httpClient: HttpClient) { }

  ngOnInit() {
    this.httpClient.get('localhost:9090/ps').subscribe(response => {
      console.log(response);
  });
  }

}
