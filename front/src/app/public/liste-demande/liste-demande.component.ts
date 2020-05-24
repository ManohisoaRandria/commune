import { Component, OnInit } from '@angular/core';
import { PublicService } from '../../service/public.service';

@Component({
  selector: 'app-liste-demande',
  templateUrl: './liste-demande.component.html',
  styleUrls: ['./liste-demande.component.scss']
})
export class ListeDemandeComponent implements OnInit {

  constructor(private publicService: PublicService) { }

  ngOnInit() {
    
  }

}
