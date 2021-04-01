import { Component, OnInit, resolveForwardRef, ViewChild } from '@angular/core';
import { FormGroup, FormControl, Validators, AbstractControl } from '@angular/forms';
import { Observable, of } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';
import { HttpClient } from '@angular/common/http';
import { environment } from 'src/environments/environment';
import { User } from './user/user';
import { promise } from 'selenium-webdriver';
import { NavigationStart, Router } from '@angular/router';
import { AuthServiceService } from './services/auth-service.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit{

  @ViewChild("closebutton") closeButton;
  @ViewChild("closebutton1") closeButton1;
  @ViewChild("closebutton2") closeButton2;

  registered : boolean;
  formSubmitted : boolean;
  signupForm: FormGroup;
  forgotPasswordForm : FormGroup;
  error: any;
  userNameNotTaken: boolean = false;
  emailNotTaken: boolean =  false;
  loginForm: FormGroup;
  showMenu : boolean = false;

  constructor(private router: Router, private httpClient: HttpClient, private authService : AuthServiceService) { 
    router.events.forEach((event)=>{
      if(event instanceof NavigationStart) {
        this.showMenu = event.url !== "/tweet" && event.url !== "/viewUser" 
        && event.url !== "/resetPassword"
      }
    });
  }
   
  ngOnInit(){
    this.signupForm = new FormGroup({
      'firstName': new FormControl('', [Validators.required]),
      'lastName': new FormControl('', [Validators.required]),
      'email': new FormControl('', [Validators.required, Validators.email], this.isEmailExist.bind(this)),
      'loginId': new FormControl('', [Validators.required], this.isLoginIdTaken.bind(this)),
      'password': new FormControl('', [Validators.required, Validators.minLength(6)]),
      'confirmPassword': new FormControl('', [Validators.required, this.isConfirmPasswordMatched.bind(this)]),
      'contactNumber': new FormControl('', [Validators.required, Validators.pattern("[0-9]*$"), 
      Validators.minLength(10), Validators.maxLength(10)])
    });

    this.forgotPasswordForm = new FormGroup({
      'userName': new FormControl('', [Validators.required], this.isUserNameExist.bind(this)),
      'newPassword': new FormControl('', [Validators.required, Validators.minLength(6)])
    });

    this.loginForm = new FormGroup({
      'loginUserId': new FormControl('', [Validators.required]),
      'loginPassword': new FormControl('', [Validators.required, Validators.minLength(6)])
    });
  }

  isEmailExist(formControl : FormControl) : Promise<any> | Observable<any> {
    const promise = new Promise((resolve,reject)=>{
      setTimeout(()=>{
        if(formControl.value.length > 0){
          this.checkEmailExists(formControl.value).subscribe((data) => {
            console.log("test" + JSON.stringify(data));
            if(data){
              this.emailNotTaken = false;
              resolve({'emailTaken':true})
            }
            else{
              this.emailNotTaken = true;
              resolve(null)
            }
          })
        }
      },1000);
    })
    return promise;
  }

  isUserNameExist(formControl: FormControl) : Promise<any> | Observable<any> {
    const promise=new Promise((resolve,reject)=>{
      setTimeout(()=>{
        if(formControl.value.length > 0){
        this.checkUserNameExists(formControl.value).subscribe((data) => {
          console.log("test" + JSON.stringify(data));
          if(data){
            resolve(null)
          }
          else{
            resolve({'userNotExist':true})
          }
        })
      }
      },1000);
    })
    return promise;
  }

  isLoginIdTaken(formControl: FormControl) : Promise<any> | Observable<any> {
    const promise=new Promise((resolve,reject)=>{
      setTimeout(()=>{
        if(formControl.value.length > 0){
        this.checkUserNameExists(formControl.value).subscribe((data) => {
          console.log("test" + JSON.stringify(data));
          if(data){
            this.userNameNotTaken = false;
            resolve({'userNameTaken':true})
          }
          else{
            this.userNameNotTaken = true;
            resolve(null)
          }
        })
      }
      },1000);
    })
    return promise;
  }

  isConfirmPasswordMatched(formControl: FormControl): { [s: string]: boolean } {
    if (this.signupForm) {
      if (formControl.value && formControl.value.length > 0 && formControl.value !== this.signupForm.get('password').value)
        return { 'nomatch': true }
    }
    return null
  }

  onSubmitSignup() {
    console.log("In submit");
    console.log(this.signupForm.value);   
    this.registered = true; 
    setTimeout(() => {
      this.registered = false;
    },3000);
    this.closeButton.nativeElement.click();
    this.addUser(this.signupForm.value).subscribe(
      (response) => {
        console.log("success");
      },
      (responseError) => {
        this.error = responseError.error.errorMessage;
      });
      this.signupForm.reset();
  }

  onSubmitForgotPassword() {
    console.log("In submit");
    console.log(this.forgotPasswordForm.value);
    this.closeButton2.nativeElement.click();
    this.forgotPassword(this.forgotPasswordForm.get("userName").value, 
      this.forgotPasswordForm.get("newPassword").value).subscribe(
      (response) => {
        console.log("success");
        alert('Password Reset Successfully! Please login');
      },
      (responseError) => {
        this.error = responseError.error.errorMessage;
      });
      this.forgotPasswordForm.reset();
  }

  onSubmitLogin() {
    console.log("In submit");
    console.log(this.loginForm.value);
    this.closeButton1.nativeElement.click();
    this.login(this.loginForm.get("loginUserId").value, this.loginForm.get("loginPassword").value).subscribe(
      (response) => {
        console.log("success"+response);
        if(response) {
          this.authService.isLoggedin = true;
          const data = JSON.parse(JSON.stringify(response));
          console.log(data.loginId)
          this.authService.loggedInUserId = data.loginId;
          this.router.navigate(['/tweet']);
        }
      },
      (responseError) => {
        this.error = responseError.error.errorMessage;
      });
      this.loginForm.reset();
  }

  login(loginUserId: string, loginPassword: string) : Observable<Object> {
    return this.httpClient.post<User>(environment.baseUrl+"login",{loginUserId,loginPassword});
  }

  forgotPassword(userName: string, newPassword: string): Observable<Object> {
    return this.httpClient.put(environment.baseUrl+userName+"/forgot",newPassword);
  }

  addUser(user: User): Observable<Object> {
    return this.httpClient.post(environment.baseUrl+"register", user);
  }

  checkUserNameExists(id : string): Observable<Object> {
    return this.httpClient.get(environment.baseUrl+"search/user/"+id).pipe(
      tap((data) => {
        console.log('username check done' + JSON.stringify(data));
      }),
      catchError( err => of([err]) )
    );
  }

  checkEmailExists(emailId : string) : Observable<Object> {
    return this.httpClient.get(environment.baseUrl+"search/email/"+emailId).pipe(
      tap((data)=>{
        console.log('email check done' + JSON.stringify(data));
      }),
      catchError(err => of([err]))
    );
  }
}

