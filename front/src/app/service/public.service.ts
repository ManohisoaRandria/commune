import { Injectable } from '@angular/core';
import { HttpClient} from '@angular/common/http';

@Injectable({
    providedIn: 'root'
})
export class PublicService {

    constructor(private httpClient: HttpClient) { }

    signIn(idUnique: string) {

    }
    getInfo(idUnique: string) {
        this.httpClient.get('localhost:8080/apropos/' + idUnique).subscribe(respone => {
            console.log(respone);
            return respone['response'];
        });
    }
    getListeDemande(idUnique: string) {
        this.httpClient.get('localhost:8080/listeDemande/' + idUnique).subscribe(respone => {
            console.log(respone);
            return respone['response'];
        });
    }
    demanderCpoie() {
        
    }
}