import {Component, OnInit} from '@angular/core';
import {NotificationSettingService} from '../shared/services/notification-setting.service';
import {NotificationSetting} from '../shared/models/settings/notification_setting';
import {ApplicationService} from '../shared/services/application.service';
import {Application} from '../shared/models/applications/application';


@Component({
  moduleId: module.id,
  selector: 'sd-settings',
  templateUrl: 'settings.component.html',
  styleUrls: ['settings.component.css']
})
export class SettingsComponent implements OnInit {
  notificationSettings:NotificationSetting[] = [];
  application:Application;
  // TODO replace
  applicationId:number = 8;

  constructor(private notificationSettingService:NotificationSettingService, private applicationService:ApplicationService) {

  }

  ngOnInit() {
    this.getNotificationSettings();
    this.getApplication(this.applicationId);
  }

  getNotificationSettings() {
    this.notificationSettingService.get()
      .subscribe(
        notificationSettings => {
          this.notificationSettings = notificationSettings;
        },
        error => {
          console.log(error);
        }
      );
  }

  getApplication(id:number) {
    this.applicationService.find(id).subscribe(
      application => {
        this.application = application;
      },
      error => {
        console.log(error);
      }
    );
  }

  toggleApplicationState(id:number) {
    this.applicationService.toggleState(id).subscribe(
      application => {
        this.application = application;
      },
      error => {
        console.log(error);
      }
    );
  }

  update(id:number, email:string, applicationId:number, active:boolean) {
    console.log(email + ", " + applicationId + ", " + active);
    let notificationSetting = new NotificationSetting(id, applicationId, email, active);

    this.notificationSettingService.update(notificationSetting)
      .subscribe(
        notificationSettings => {
          this.getNotificationSettings();
        },
        error => {
          console.log(error);
        }
      );
  }

  create(email:string, applicationId:number, active:boolean) {
    console.log(email + ", " + applicationId + ", " + active);
    let notificationSetting = new NotificationSetting(null, applicationId, email, active);

    this.notificationSettingService.create(notificationSetting)
      .subscribe(
        notificationSettings => {
          this.getNotificationSettings();
        },
        error => {
          console.log(error);
        }
      );
  }
}
