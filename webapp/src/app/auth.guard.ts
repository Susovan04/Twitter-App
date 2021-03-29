import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, Router } from '@angular/router';
import { Observable, Observer } from 'rxjs';
import { AuthServiceService } from './services/auth-service.service';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {
  constructor(private router: Router, private authService : AuthServiceService) { }
  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean> | Promise<boolean> | boolean {
      console.log('URL', state.url);
      return Observable.create((observer: Observer<boolean>) => {
        if (this.authService.isLoggedin){
          console.log('logged in');
          observer.next(true);
        } else {
          console.log('Not logged in');
          this.router.navigate(['/']);
        }
      });
  }
}
