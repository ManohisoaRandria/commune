import { HttpClientModule } from '@angular/common/http';
import { BrowserModule } from '@angular/platform-browser';
import { Routes, RouterModule } from '@angular/router';

import { AppComponent } from './app.component';
import { LoginPersonneComponent } from './public/login-personne/login-personne.component';
import { InfoComponent } from './public/info/info.component';
import { ListeDemandeComponent } from './public/liste-demande/liste-demande.component';
import { DemandeCopieComponent } from './public/demande-copie/demande-copie.component';
import { LoginEmployeeComponent } from './commune/login-employee/login-employee.component';
import { InscriptionEmployeeComponent } from './commune/inscription-employee/inscription-employee.component';
import { InscriptionPersonneComponent } from './commune/inscription-personne/inscription-personne.component';
import { DemandeComponent } from './commune/demande/demande.component';
import { PublicComponent } from './public/public/public.component';
import { CommuneComponent } from './commune/commune/commune.component';

import { PublicService } from './service/public.service';
import { CommuneService } from './service/commune.service';
import { NgModule } from '@angular/core';

const appRoutes: Routes = [
  {path: '', component: LoginPersonneComponent},
  {path: 'LoginPersonne', component: LoginPersonneComponent},
  {path: 'LoginEmployee', component: LoginEmployeeComponent},
  {path: 'Info', component: InfoComponent},
  {path: 'ListeDemande', component: ListeDemandeComponent},
  {path: 'DemandeCopie', component: DemandeCopieComponent},
  {path: 'InscriptionEmployee', component: InscriptionEmployeeComponent},
  {path: 'InscriptionPersonne', component: InscriptionPersonneComponent},
  {path: 'Demande', component: DemandeComponent},
  {path: '**', redirectTo: '/LoginPersonne'}
];

@NgModule({
  declarations: [
    AppComponent,
    LoginPersonneComponent,
    InfoComponent,
    ListeDemandeComponent,
    DemandeCopieComponent,
    LoginEmployeeComponent,
    InscriptionEmployeeComponent,
    InscriptionPersonneComponent,
    DemandeComponent,
    PublicComponent,
    CommuneComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    RouterModule.forRoot(appRoutes)
  ],
  providers: [
    PublicService,
    CommuneService
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
