import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import {SettingsComponent} from './settings.component';


@NgModule({
    imports: [CommonModule, RouterModule],
    declarations: [SettingsComponent],
    exports: [SettingsComponent],
    providers: []
})

export class SettingsModule { }
