export class FeedbackStatus {
  id:number;
  apiUserId:number;
  feedbackId:number;
  status:string;

  constructor(id:number, apiUserId:number, feedbackId:number, status:string) {
    this.id = id;
    this.apiUserId = apiUserId;
    this.feedbackId = feedbackId;
    this.status = status;
  }
}
