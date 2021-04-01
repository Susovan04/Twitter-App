import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthServiceService } from '../services/auth-service.service';
import { ViewUserComponent } from '../view-user/view-user.component';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {

  constructor(private router : Router, private authService : AuthServiceService) { }

  ngOnInit(): void {
  }

  isAuthenticated() : boolean {
    return this.authService.isLoggedin;
  }

  onSignout() {
    this.router.navigate(["/"]);
    this.authService.isLoggedin = false;
    this.authService.loggedInUserId = null;
  }


}
