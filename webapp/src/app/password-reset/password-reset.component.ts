import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { AuthServiceService } from '../services/auth-service.service';

@Component({
  selector: 'app-password-reset',
  templateUrl: './password-reset.component.html',
  styleUrls: ['./password-reset.component.css']
})
export class PasswordResetComponent implements OnInit {

  passwordResetForm : FormGroup;
  error: any;

  constructor(private http : HttpClient, private authService : AuthServiceService) { }

  ngOnInit(){

    this.passwordResetForm = new FormGroup({
      'newPwd': new FormControl('', [Validators.required, Validators.minLength(6)]),
      'confirmPwd': new FormControl('', [Validators.required, this.isConfirmPasswordMatched.bind(this)])
    });

  }

  isConfirmPasswordMatched(formControl: FormControl): { [s: string]: boolean } {
    if (this.passwordResetForm) {
      if (formControl.value && formControl.value.length > 0 
        && formControl.value !== this.passwordResetForm.get('newPwd').value)
        return { 'nomatch': true }
    }
    return null
  }

  onSubmitPasswordReset() {
    console.log("In password reset submit")
    console.log(this.passwordResetForm.value);
    this.passwordReset(this.passwordResetForm.get("newPwd").value).subscribe(
      (response) => {
        console.log("success");
        alert('Password Reset Successfully! Please login');
      },
      (responseError) => {
        this.error = responseError.error.errorMessage;
      });
      this.passwordResetForm.reset();
  }

  passwordReset(password: string) : Observable<Object> {
    return this.http.put(environment.baseUrl+this.authService.loggedInUserId+"/reset",password);
  }

}
