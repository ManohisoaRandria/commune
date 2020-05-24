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

  onSubmit(id) {
    console.log(id);
    this.http.get('http://localhost:8080/apropos/'+id).subscribe(res=>{
      this.resp=res;
      console.log(this.resp.response);
    });
    //this.router.navigate(['LoginEmployee']);
  }
  go1() {
    this.router.navigate(['Info']);
  }

}
