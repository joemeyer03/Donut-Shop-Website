import { Component, OnInit } from '@angular/core';
import { Product } from '../product';
import { ProductService } from '../product.service';
import { LoginService } from '../login.service';
import { CartService } from '../cart.service';
import { ValueFromArray } from 'rxjs';

@Component({
  selector: 'app-admin',
  templateUrl: './admin.component.html',
  styleUrls: ['./admin.component.css']
})
export class AdminComponent implements OnInit {

  private user!: string;
  products: Product[] = [];

  constructor(private service: LoginService, private productService: ProductService, 
    private cartService: CartService) { }

  ngOnInit(): void {
    this.getProducts();
  }

  getProducts(): void {
    this.productService.getProducts()
    .subscribe(products => this.products = products);
  }

  add(name: string, p: string, q: string): void {
    name = name.trim();
    if (!name) { return; }
    if (!p) { return; }
    if (!q) { return; }
    
    var price : Number;
    price = Number(p);
    var quantity : Number;
    quantity = Number(q);
    
    this.productService.addProduct({name, price, quantity} as Product)
      .subscribe(product => {
        this.products.push(product);
      });
  }

  delete(product: Product): void {
    this.cartService.remove(product);
    this.products = this.products.filter(h => h !== product);
    this.productService.deleteProduct(product.name).subscribe();
  }

  isAdminLoggedIn(): boolean {
    return this.service.getCurrentUser() === 'admin'; 
  }
}
