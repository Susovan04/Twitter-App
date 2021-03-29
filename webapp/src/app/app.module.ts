import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { TweetComponent } from './tweet/tweet.component';
import { HeaderComponent } from './header/header.component';
import { RouterModule, Routes } from '@angular/router';
import { ViewUserComponent } from './view-user/view-user.component';
import { PasswordResetComponent } from './password-reset/password-reset.component';
import { AuthGuard } from './auth.guard';

const routes: Routes = [{ path: 'tweet', component: TweetComponent, canActivate: [AuthGuard] },
{path:'viewUser', component : ViewUserComponent, canActivate: [AuthGuard]},
{path:'resetPassword', component : PasswordResetComponent, canActivate: [AuthGuard]}
];

@NgModule({
  declarations: [
    AppComponent,
    TweetComponent,
    HeaderComponent,
    ViewUserComponent,
    PasswordResetComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    [RouterModule.forRoot(routes)],
    ReactiveFormsModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
