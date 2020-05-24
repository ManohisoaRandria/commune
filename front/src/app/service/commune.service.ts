import { Injectable } from '@angular/core';
import { HttpClient} from '@angular/common/http';

@Injectable({
    providedIn: 'root'
})
export class CommuneService {
    constructor(private httpClient: HttpClient) { }

    signIn(pseudo: string, pwd: string) {

    }
}