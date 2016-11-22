

export class NotificationSetting {
  id:number;
  applicationId:number;
  email:string;
  active:boolean;

  constructor(id:number, applicationId:number, email:string, active:boolean) {
    this.id = id;
    this.applicationId = applicationId;
    this.email = email;
    this.active = active;
  }

  toUnderscore():{} {
    return {
      id: this.id,
      application_id: this.applicationId,
      email: this.email,
      active: this.active,
    }
  }
}
