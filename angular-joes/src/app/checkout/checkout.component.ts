import { Component, OnInit } from '@angular/core';
import { CartService } from '../cart.service';
import { LoginService } from '../login.service';
import { CheckoutService } from '../checkout.service';
import { Product } from '../product';

@Component({
  selector: 'app-checkout',
  templateUrl: './checkout.component.html',
  styleUrls: ['./checkout.component.css']
})
export class CheckoutComponent implements OnInit {

  initalized: boolean = false;
  checkouts: Product[][] = [];
  curStatus: string = "idunno";

  constructor(private loginService: LoginService, private cartService: CartService, private checkoutService: CheckoutService) { }

  ngOnInit(): void {
  }

  checkout(): void {
    if (this.cartService.getItems().length == 0) return;
    this.checkoutService.addUserCheckout(this.loginService.getCurrentUser(), this.cartService.getItems()).subscribe((result: Product[][]) => {this.checkouts = result});
    this.cartService.clear();
    this.curStatus = this.checkouts.length.toString();
    this.initalized = false;
    
  }

  getHasCart(): boolean {
    if (this.cartService.getItems() != null) return this.cartService.getItems().length > 0;
    return false;
  }

  getProductsInCart(): Product[] {
    return this.cartService.getItems();
  }

  getPrice(p: Product): number {
    return this.cartService.getPrice(p);
  }

  getQuantity(p: Product): number {
    return p.quantity;
  }

  getTotal(): number {
    return this.cartService.getTotal();
  }

  getOrders(): Product[][] {
    //this.curStatus = "one";

    if (!this.initalized) {
      this.checkoutService.getUserCheckouts(this.loginService.getCurrentUser()).subscribe((result: Product[][]) => { this.checkouts = result });
      this.initalized = true;
    }

    this.curStatus = this.checkouts.length.toString();

    return this.checkouts;
  }

}
