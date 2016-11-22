import {Component, OnInit} from '@angular/core';
import {Feedback} from '../shared/models/feedbacks/feedback';
import {Application} from '../shared/models/applications/application';
import {FeedbackListService} from '../shared/services/feedback-list.service';
import {ApplicationService} from '../shared/services/application.service';
import {TextMechanism} from '../shared/models/mechanisms/text_mechanism';
import {RatingMechanism} from '../shared/models/mechanisms/rating_mechanism';
import {ActivatedRoute, Router} from '@angular/router';
import {FeedbackStatusService} from '../shared/services/feedback-status.service';
import {FeedbackStatus} from '../shared/models/feedbacks/feedback_status';
import {ApplicationFilterService} from '../shared/services/application-filter.service';
import {Location} from '@angular/common';


@Component({
  moduleId: module.id,
  selector: 'sd-feedback-list',
  templateUrl: 'feedback-list.component.html',
  styleUrls: ['feedback-list.component.css'],
})

export class FeedbackListComponent implements OnInit {
  errorMessage:string;
  feedbacks:Feedback[] = [];
  filteredFeedbacks:Feedback[] = [];
  selectedFeedbacks:Feedback[] = [];
  applications:Application[] = [];
  selectedApplication:Application;
  sortOrder:{} = {'id': '', 'title': '', 'date': ''};
  readingStateFilter:string = 'all';
  loaded:boolean = false;

  constructor(public feedbackListService:FeedbackListService, private applicationService:ApplicationService,
              private router:Router, private route:ActivatedRoute, private feedbackStatusService:FeedbackStatusService,
              private applicationFilterService:ApplicationFilterService, private location:Location) {
    this.getApplications();
  }

  ngOnInit() {

  }

  getFeedbacks(applicationId:number) {
    this.feedbackListService.get(applicationId)
      .subscribe(
        feedbacks => {
          this.feedbacks = feedbacks;
          this.filteredFeedbacks = feedbacks;
          this.sortFeedbacks('id', false);
          this.loaded = true;
        },
        error => {
          console.log(error);
          if(error.status === 403) {
            this.router.navigate(['/login'])
          }
        }
      );
  }

  getApplications() {
    this.applicationService.all().subscribe(
      applications => {
        this.applications = applications;
        for (var application of this.applications) {
          application.filterActive = false;
        }
        this.populateConfigurationData();
        let selectedApplication = this.applicationFilterService.getApplication();
        if(selectedApplication ) {
          this.clickedApplicationFilter(selectedApplication );
        }
      },
      error => this.errorMessage = <any>error
    );
  }

  resetSortOrder():void {
    this.sortOrder = {'id': '', 'title': '', 'date': ''};
  }

  sortClicked(field:string) {
    if (this.sortOrder[field] === '') {
      this.resetSortOrder();
      this.sortFeedbacks(field, true);
      this.sortOrder[field] = 'asc';
    } else if (this.sortOrder[field] === 'asc') {
      this.resetSortOrder();
      this.sortFeedbacks(field, false);
      this.sortOrder[field] = 'desc';
    } else if (this.sortOrder[field] === 'desc') {
      this.resetSortOrder();
      this.sortOrder[field] = 'asc';
      this.sortFeedbacks(field, true);
    }
  }

  sortFeedbacks(field:string, ascending:boolean = true) {
    var feedbacks = this.filteredFeedbacks.sort(function (feedbackA, feedbackB) {
      if (field === 'date') {
        var dateA:any = new Date(feedbackA[field]);
        var dateB:any = new Date(feedbackB[field]);
        return dateA - dateB;
      } else {
        return feedbackA[field] - feedbackB[field];
      }
    });

    if (!ascending) {
      this.filteredFeedbacks = feedbacks.reverse();
    } else {
      this.filteredFeedbacks = feedbacks;
    }
  }

  search(filterString:string) {
    this.filteredFeedbacks = this.feedbacks.filter(item => item.title.toLowerCase().indexOf(filterString.toLowerCase()) !== -1);
  }

  clickedApplicationFilter(application) {
    this.selectedApplication = application;
    this.getFeedbacks(application.id);
    this.applicationFilterService.setApplication(application);
  }

  /**
   * combines repository with the orchestrator data
   */
  populateConfigurationData() {
    for(var feedback of this.feedbacks) {
      var application = this.applications.filter(application => application.id === feedback.applicationId)[0];
      if(application) {
        var configuration = application.configurations.filter(configuration => configuration.id === feedback.configurationId)[0];

        for(var textFeedback of feedback.textFeedbacks) {
          let textMechanism:TextMechanism = <TextMechanism>configuration.mechanisms.filter(mechanism => mechanism.id === textFeedback.mechanismId)[0];
          textFeedback.mechanism = new TextMechanism(textMechanism.id, textMechanism.type, textMechanism.active, textMechanism.order, textMechanism.canBeActivated, textMechanism.parameters);
        }
        for(var ratingFeedback of feedback.ratingFeedbacks) {
          let ratingMechanism:RatingMechanism = <RatingMechanism>configuration.mechanisms.filter(mechanism => mechanism.id === ratingFeedback.mechanismId)[0];
          ratingFeedback.mechanism = new RatingMechanism(ratingMechanism.id, ratingMechanism.type, ratingMechanism.active, ratingMechanism.order, ratingMechanism.canBeActivated, ratingMechanism.parameters);
        }
      }
    }
  }

  exportAsCSV():void {
    var csvContent = "";
    csvContent += "ID, Title, Date, Application, Feedbacks \n";

    for (let feedback of this.selectedFeedbacks) {
      csvContent += feedback.id + "," + feedback.title + "," + feedback.createdAt + "," + feedback.applicationId + ",";

      if(feedback.textFeedbacks) {
        for(let textFeedback of feedback.textFeedbacks) {
          if(textFeedback.mechanism) {
            csvContent += textFeedback.mechanism.getParameterValue('title') + ": " + textFeedback.text + ",";
          } else {
            csvContent += textFeedback.text + ",";
          }
        }
      }

      if(feedback.ratingFeedbacks) {
        for(let ratingFeedback of feedback.ratingFeedbacks) {
          if(ratingFeedback.mechanism) {
            csvContent += ratingFeedback.mechanism.getParameterValue('title') + ": " + ratingFeedback.rating + "/" + ratingFeedback.mechanism.getParameterValue('maxRating') + ",";
          } else {
            csvContent += ratingFeedback.rating + ",";
          }
        }
      }

      // remove last comma
      csvContent = csvContent.slice(0, -1);
      csvContent += "\n";
    }

    var title = 'feedbacks';
    var filename = title.replace(/ /g, '') + '.csv';
    var blob = new Blob([csvContent], {"type": 'text/csv;charset=utf-8;'});
    if (navigator.msSaveBlob) { // IE 10+
      navigator.msSaveBlob(blob, filename);
    } else {
      var link = document.createElement("a");
      if (link.download !== undefined)
      {
        var url = URL.createObjectURL(blob);
        link.setAttribute("href", url);
        link.setAttribute("download", filename);
        link.style.visibility = 'hidden';
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
      }
    }
  }

  select(event, feedback:Feedback) {
    var index = this.selectedFeedbacks.indexOf(feedback);

    if(index > -1) {
        this.selectedFeedbacks.splice(index, 1);
    } else {
      this.selectedFeedbacks.push(feedback);
    }
  }

  markAsReadOrUnread(feedbacks:Feedback[], read:boolean):void {
    for(let feedback of feedbacks) {
      let applicationId = feedback.applicationId;
      if(feedback.feedbackStatuses.length > 0 && feedback.feedbackStatuses.filter(feedbackStatus => feedbackStatus.status === 'read' || feedbackStatus.status === 'unread').length > 0) {
        let feedbackStatus = feedback.feedbackStatuses.filter(feedbackStatus => feedbackStatus.status === 'read' || feedbackStatus.status === 'unread')[0];
        this.feedbackStatusService.updateReadStatus(read, feedbackStatus.id, feedbackStatus.feedbackId, applicationId).subscribe(
          result => {
            if (!read) {
              this.location.back();
            }
          },
          error => {
            console.log(error);
          }
        );
      }
    }
  }

  filterByRead(read:boolean) {
    if(this.readingStateFilter === 'read') {
      if(read) {
        this.readingStateFilter = 'all';
        this.filteredFeedbacks = this.feedbacks;
      } else {
        this.readingStateFilter = 'unread';
        this.filteredFeedbacks = this.feedbacks.filter(feedback => feedback.read === read);
      }
    } else if(this.readingStateFilter === 'unread') {
      if(read) {
        this.readingStateFilter = 'read';
        this.filteredFeedbacks = this.feedbacks.filter(feedback => feedback.read === read);
      } else {
        this.readingStateFilter = 'all';
        this.filteredFeedbacks = this.feedbacks;
      }
    } else if(this.readingStateFilter === 'all') {
      if(read) {
        this.readingStateFilter = 'read';
      } else {
        this.readingStateFilter = 'unread';
      }
      this.filteredFeedbacks = this.feedbacks.filter(feedback => feedback.read === read);
    }
  }
}

















