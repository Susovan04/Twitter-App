import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { User } from '../user/user';

@Component({
  selector: 'app-view-user',
  templateUrl: './view-user.component.html',
  styleUrls: ['./view-user.component.css']
})
export class ViewUserComponent implements OnInit {

  users : User[];

  constructor(private http : HttpClient) { }

  ngOnInit(){
      this.getAllUsers().subscribe(
        (response) => {
          console.log(response);
          this.users = response;
        }
      );
      }
      getAllUsers() : Observable<any[]> {
        return this.http.get<User[]>(environment.baseUrl+"users/all");
      }
}

