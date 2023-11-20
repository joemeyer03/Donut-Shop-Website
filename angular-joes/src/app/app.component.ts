import { Component } from '@angular/core';
import { LoginService } from './login.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'angular-joes';

  private user!: string;

  constructor(private service: LoginService) { }

  isAdminLoggedIn(): boolean {
    this.user = this.service.getCurrentUser();
    return !(this.user == null) && (this.user === "admin");
  }

  isOnDashboard(): boolean {
    return window.location.href == 'http://localhost:4200/dashboard';
  }
}
