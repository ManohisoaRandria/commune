import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';


@Component({
  selector: 'app-login-personne',
  templateUrl: './login-personne.component.html',
  styleUrls: ['./login-personne.component.scss']
})
export class LoginPersonneComponent implements OnInit {
resp:any;
  constructor(private router: Router,private http:HttpClient) { }

  ngOnInit() {
  }

  onSubmit(idUnique: string) {
    localStorage.setItem("idUnique", idUnique);
    this.router.navigate(['Info']);
  }

}
