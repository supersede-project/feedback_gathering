import {Component, OnInit} from '@angular/core';
import {UserService} from './shared/services/user.service';

/**
 * This class represents the main application component. Within the @Routes annotation is the configuration of the
 * applications routes, configuring the paths for the lazy loaded components (HomeComponent, AboutComponent).
 */
@Component({
  moduleId: module.id,
  selector: 'sd-app',
  templateUrl: 'app.component.html',
})

export class AppComponent implements OnInit {

  constructor(public userService:UserService) {

  }
}
