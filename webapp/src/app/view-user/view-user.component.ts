import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { AuthServiceService } from '../services/auth-service.service';
import { User } from '../user/user';

@Component({
  selector: 'app-view-user',
  templateUrl: './view-user.component.html',
  styleUrls: ['./view-user.component.css']
})
export class ViewUserComponent implements OnInit {

  users : User[];

  constructor(private http : HttpClient, private authService : AuthServiceService) { }

  ngOnInit(){
      this.getAllUsers().subscribe(
        (response) => {
          console.log(response);
          this.users = response;
        }
      );
      }
      getAllUsers() : Observable<any[]> {
        const httpOptions={
          headers:new HttpHeaders({
            'content-type':'application/json',
            'Authorization':'Bearer ' + this.authService.getToken()
          })
        };
        return this.http.get<User[]>(environment.baseUrl+"users/all",httpOptions);
      }
}

