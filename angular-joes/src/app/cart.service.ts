import { Injectable } from '@angular/core';
import { Product } from './product';
import { Observable, of } from 'rxjs';

import { catchError, map, tap } from 'rxjs/operators';
import { Cart } from './cart';
import { LoginService } from './login.service';
import { ProductService } from './product.service';

@Injectable({
  providedIn: 'root',
})
export class CartService {
    cart!: Cart;
    private user: string = '';
    products: Product[] = [];

    constructor(private loginService: LoginService, private productService: ProductService){
        if (this.isUserLoggedIn()) {
            this.makeCart();
        }
    }

    makeCart(): void {
        this.loginService.getUserCart(this.loginService.getCurrentUser()).subscribe((cart) => {this.products = cart; 
            this.setCart(this.products)});
    }

    getCart(): Cart {
        return this.cart;
    }

    setCart(p: Product[]): void {
        this.cart = new Cart(p);
    }

    updateCart(): void {
        this.loginService.updateUserCart(this.getUserName(), this.getItems())
            .subscribe();
    }

    setUserName(user: string) {
        this.user = user;
    }

    getUserName(): string {
        return this.user;
    }

    getItems(): Product[] {
        return this.cart.getItems();
    }

    addToCart(p: Product): void{
       if (this.isUserLoggedIn()) {
        this.cart.addItem(p);
        this.updateCart();
       } else {
        window.alert("Please log in before adding to cart.");
       }
    }

    increase(p: Product): void {
        this.cart.increaseQuantity(p);
        this.updateCart();
    }

    decrease(p: Product): void {
        this.cart.decreaseQuantity(p);
        this.updateCart();
    }

    remove(p: Product): void {
        this.cart.remove(p);
        this.updateCart();
    }

    getQuantity(p: Product): number {
        return this.cart.getQuantity(p);
    }

    updatePrice(p: Product): void {
        this.productService.getProduct(p.name)
            .subscribe((product) => this.cart.updateProductPrice(p, product.price));
    }

    getPrice(p: Product): number {
        return this.cart.getPrice(p);
    }

    clear(): void {
        this.cart.clearItems();
        this.updateCart();
    }

    getTotal(): number {
        return this.cart.getTotal();
    }

    isUserLoggedIn(): boolean {
        this.user = this.loginService.getCurrentUser();
        return !(this.user === '');
    } 

    isAdminLoggedIn(): boolean {
        this.user = this.loginService.getCurrentUser();
        return !(this.user == null) && (this.user === "admin");
    } 
}