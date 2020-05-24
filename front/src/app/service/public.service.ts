import { Injectable } from '@angular/core';
import { HttpClient} from '@angular/common/http';

@Injectable({
    providedIn: 'root'
})
export class PublicService {

    constructor(private httpClient: HttpClient) { }

    getInfo(idUnique: string) {
        this.httpClient.get('localhost:8080/apropos/' + idUnique).subscribe(response => {
            console.log(response);
            return response['response'];
        });
    }
    getListeDemande(idUnique: string) { /*
        this.httpClient.get('localhost:8080/listeDemande/' + idUnique).subscribe(response => {
            console.log(response);
            return response['response'];
        }); */
        this.httpClient.get('localhost:9090/ps').subscribe(response => {
            console.log(response);
            return response['response'];
        });
    }
    demanderCpoie(idUnique: string, nbr: number) {
        var demande = {
            idU: idUnique,
            nbCopie: nbr
        }
        this.httpClient.post('localhost:8080/faireDemande', demande).subscribe(response => {
            console.log(response);
            console.log('demande envoyee');
            return response['message'];
        });
    }
}