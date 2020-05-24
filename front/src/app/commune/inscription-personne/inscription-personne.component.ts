import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-inscription-personne',
  templateUrl: './inscription-personne.component.html',
  styleUrls: ['./inscription-personne.component.scss']
})
export class InscriptionPersonneComponent implements OnInit {

  constructor() { }

  ngOnInit() {
  }

  onSubmit(idUnique: string, nom: string, prenom: string, dateNaissance: string, lieuNaissance: string,
    heureNaissance: string, pere: string, mere: string, idCommune: string) {

  }

}
