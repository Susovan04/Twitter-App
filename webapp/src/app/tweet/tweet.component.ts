import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { AuthServiceService } from '../services/auth-service.service';

@Component({
  selector: 'app-tweet',
  templateUrl: './tweet.component.html',
  styleUrls: ['./tweet.component.css']
})
export class TweetComponent implements OnInit {

  postTweetForm : FormGroup;

  constructor(private http : HttpClient, private authService : AuthServiceService) { }

  ngOnInit(){
    this.postTweetForm = new FormGroup({
      "tweet" : new FormControl('', [Validators.required, Validators.maxLength(144)])
    });
  }

  onSubmitTweet() {
    console.log(this.postTweetForm.value);
    this.postTweet(this.postTweetForm.get("tweet").value).subscribe(
      (response) => {
        console.log("tweet posted");
      }
    );
    this.postTweetForm.reset();
  }
  postTweet(tweet: string) : Observable<Object>{
    return this.http.post(environment.baseUrl+this.authService.loggedInUserId+"/add",tweet);
  }
}
