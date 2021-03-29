import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class AuthServiceService {

  isLoggedin : boolean = false;

  constructor() { }
}
