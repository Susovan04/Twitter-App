import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { User } from '../user/user';

@Injectable({
  providedIn: 'root'
})
export class AuthServiceService {

  isLoggedin : boolean = false;
  loggedInUserId: string = null;
  private token: string;

  constructor(private httpClient: HttpClient) { }

  login(loginUserId: string, loginPassword: string) : Observable<any> {
    let credentials = btoa(loginUserId + ":" + loginPassword);
    let headers = new HttpHeaders();
    headers = headers.set('Authorization', 'Basic ' + credentials);
    return this.httpClient.get(environment.baseUrl+"authenticate/login",{headers});
  }

  public setToken(token: string) {
    this.token = token;
  }
  public getToken() {
    return this.token;
  }
}
