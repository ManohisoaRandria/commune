import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login-personne',
  templateUrl: './login-personne.component.html',
  styleUrls: ['./login-personne.component.scss']
})
export class LoginPersonneComponent implements OnInit {

  constructor(private router: Router) { }

  ngOnInit() {
  }

  onSubmit(idUnique: string) {
    localStorage.setItem("idUnique", idUnique);
    this.router.navigate(['Info']);
  }

}
