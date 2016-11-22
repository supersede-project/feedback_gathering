import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import {SettingsComponent} from './settings.component';
import {NotificationSettingService} from '../shared/services/notification-setting.service';


@NgModule({
    imports: [CommonModule, RouterModule],
    declarations: [SettingsComponent],
    exports: [SettingsComponent],
    providers: [NotificationSettingService]
})

export class SettingsModule { }
