import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Component, OnInit, ViewChild } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { AuthServiceService } from '../services/auth-service.service';
import { Tweet } from '../tweet';

@Component({
  selector: 'app-my-tweets',
  templateUrl: './my-tweets.component.html',
  styleUrls: ['./my-tweets.component.css']
})
export class MyTweetsComponent implements OnInit {

  tweetReplyForm : FormGroup;
  tweets : Tweet[];
  tweetId : any = null;

  @ViewChild("closebutton") closeButton;

  constructor(private http : HttpClient, private authService : AuthServiceService) { }

  ngOnInit(){

    this.tweetReplyForm = new FormGroup({
      "reply" : new FormControl('', [Validators.required, Validators.maxLength(144)])
    });

    this.getAllMyTweets().subscribe(
      (response) => {
        console.log(response);
        this.tweets = response;
      }
    );

  }

  onSubmitTweetReply() {
    console.log(this.tweetReplyForm.value);
    this.closeButton.nativeElement.click();
    this.postReply(this.tweetReplyForm.get("reply").value).subscribe(
      (response) => {
        console.log(response);
        if(response) {
          this.getAllMyTweets().subscribe(
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
    const httpOptions={
      headers:new HttpHeaders({
        'content-type':'application/json',
        'Authorization':'Bearer ' + this.authService.getToken()
      })
    };
    return this.http.post(environment.baseUrl+this.authService.loggedInUserId+"/reply/"+this.tweetId,reply,httpOptions);
  }

  showModal(id : string) {
    console.log(id);
    this.tweetId = id;
  }

  getAllMyTweets() : Observable<any[]> {
    const httpOptions={
      headers:new HttpHeaders({
        'content-type':'application/json',
        'Authorization':'Bearer ' + this.authService.getToken()
      })
    };
    return this.http.get<Tweet[]>(environment.baseUrl+this.authService.loggedInUserId,httpOptions);
  }

}
