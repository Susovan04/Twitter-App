import { HttpClient } from '@angular/common/http';
import { Component, OnInit, ViewChild } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { AuthServiceService } from '../services/auth-service.service';
import { Tweet } from '../tweet';

@Component({
  selector: 'app-tweet',
  templateUrl: './tweet.component.html',
  styleUrls: ['./tweet.component.css']
})
export class TweetComponent implements OnInit {

  postTweetForm : FormGroup;
  tweetReplyForm : FormGroup;
  tweets : Tweet[];
  tweetId : any = null;

  @ViewChild("closebutton") closeButton;
  
  constructor(private http : HttpClient, private authService : AuthServiceService) { }

  ngOnInit(){
    this.postTweetForm = new FormGroup({
      "tweet" : new FormControl('', [Validators.required, Validators.maxLength(144)])
    });

    this.tweetReplyForm = new FormGroup({
      "reply" : new FormControl('', [Validators.required, Validators.maxLength(144)])
    });

    this.getAllTweets().subscribe(
      (response) => {
        console.log(response);
        this.tweets = response;
      }
    );
  }

  getAllTweets() : Observable<any[]> {
    return this.http.get<Tweet[]>(environment.baseUrl+"tweets/all");
  }

  onSubmitTweet() {
    console.log(this.postTweetForm.value);
    this.postTweet(this.postTweetForm.get("tweet").value).subscribe(
      (response) => {
        if(response) {
          console.log("tweet posted");
          this.getAllTweets().subscribe(
            (response) => {
              console.log(response);
              this.tweets = response;
            }
          );
        } else {
          alert("Error Occured. Can't post tweet")
        }
      }
    );
    this.postTweetForm.reset();
  }

  postTweet(tweet: string) : Observable<Object>{
    return this.http.post(environment.baseUrl+this.authService.loggedInUserId+"/add",tweet);
  }

  onSubmitTweetReply() {
    console.log(this.tweetReplyForm.value);
    this.closeButton.nativeElement.click();
    this.postReply(this.tweetReplyForm.get("reply").value).subscribe(
      (response) => {
        console.log(response);
        if(response) {
          this.getAllTweets().subscribe(
            (response) => {
              console.log(response);
              this.tweets = response;
            }
          );
        } else {
          alert("Error Occured while adding reply");
        }       
      }
    );
    this.tweetReplyForm.reset();
  }

  postReply(reply: string) : Observable<Object>{
    return this.http.post(environment.baseUrl+this.authService.loggedInUserId+"/reply/"+this.tweetId,reply);
  }

  showModal(id : string) {
    console.log(id);
    this.tweetId = id;
  }
}
