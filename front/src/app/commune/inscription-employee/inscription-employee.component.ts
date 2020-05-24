import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-inscription-employee',
  templateUrl: './inscription-employee.component.html',
  styleUrls: ['./inscription-employee.component.scss']
})
export class InscriptionEmployeeComponent implements OnInit {

  constructor() { }

  ngOnInit() {
  }

  onSubmit(nom: string, prenom: string, pwd: string, idCommune: string, idDroit: string) {

  }
}
