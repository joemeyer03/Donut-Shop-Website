import { Component, OnInit } from '@angular/core';
import { Product } from '../product';
import { LoginService } from '../login.service';
import { CartService } from '../cart.service';
import { delay } from 'rxjs';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})

export class LoginComponent implements OnInit {

  //This cart is local, just for display purpses, don't try to change it, use login.service to change cart
  cart: Product[] = [];
  username: string = '';
  initalized: boolean = false;

  getCartLength(): number {
    return this.cart.length;
  }

  
  onEnter(username: string) {
    this.username = username;
    this.loginService.setCurrentUser(this.username);
    this.loginService.getUserCart(this.loginService.getCurrentUser()).subscribe((cart) => {this.cart = cart; 
      this.cartService.setCart(this.cart)});
  }

  public getHasUser(): boolean {
    return this.username != '';
  }

  public logout(): void {
    this.username = '';
    this.loginService.setCurrentUser(this.username);
    this.cart = [];
  }

  public getUsername(): string {
    return this.username;
  }

  constructor(private loginService: LoginService, private cartService: CartService) { 
  }

  ngOnInit(): void {
    if (!this.initalized && this.loginService.getCurrentUser() != '') {
      this.username = this.loginService.getCurrentUser();
      this.loginService.getUserCart(this.loginService.getCurrentUser()).subscribe((cart) => {this.cart = cart; 
        this.cartService.setCart(this.cart)});
      this.initalized = true;
    }
  }

}