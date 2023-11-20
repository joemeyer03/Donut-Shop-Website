import { Component, OnInit } from '@angular/core';
import { Product } from '../product';
import { Cart } from '../cart';
import { CartService } from '../cart.service';
import { LoginService } from '../login.service';

@Component({
  selector: 'app-cart',
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.css']
})
export class CartComponent implements OnInit {
  items: Product[] = [];
  cart!: Cart;

  constructor(private cartService: CartService, private loginService: LoginService) {
    if (this.isUserLoggedIn()) {
      this.cart = this.cartService.getCart();
      this.items = this.cart.getItems();
    } else {
      this.cart = new Cart([]);
    }
  }

  ngOnInit(): void {
  }

  getItems(): Product[] {
    return this.items;
  }

  isAdminLoggedIn(): boolean {
    return this.cartService.isAdminLoggedIn();
  }

  isUserLoggedIn(): boolean {
    return this.cartService.isUserLoggedIn();
  }
  
  increase(p: Product): void {
    this.cartService.increase(p);
  }

  decrease(p: Product): void {
    this.cartService.decrease(p);
  }

  remove(p: Product): void {
    this.cartService.remove(p);
  }

  getQuantity(p: Product): number {
    return this.cartService.getQuantity(p);
  }

  getPrice(p: Product): number {
    return this.cartService.getPrice(p);
  }

  clear(): void {
    this.cartService.clear();
    this.items = this.cartService.getItems();
  }

  getTotal(): number {
    return this.cartService.getTotal();
  }
}
