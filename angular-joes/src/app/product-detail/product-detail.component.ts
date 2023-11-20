import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Location } from '@angular/common';

import { Product } from '../product';
import { ProductService } from '../product.service';
import { CartService } from '../cart.service';
import { Cart } from '../cart';
import { CartComponent } from '../cart/cart.component';
import { LoginComponent } from '../login/login.component';
import { LoginService } from '../login.service';
import { Review } from '../review';
import { ReviewService } from '../review.service';

@Component({
  selector: 'app-product-detail',
  templateUrl: './product-detail.component.html',
  styleUrls: [ './product-detail.component.css' ]
})
export class ProductDetailComponent implements OnInit {
  product!: Product;
  reviews!: Review;
  stocked = this.inStock();
  reviewToAdd!: string;

  status = "hello";

  constructor(
    private route: ActivatedRoute,
    private productService: ProductService,
    private location: Location,
    private cartService: CartService,
    private loginService: LoginService,
    private reviewService: ReviewService,
  ) {}

  ngOnInit(): void {
    this.getProduct();
    this.getReview();
  }

  getProduct(): void {
    const name = this.route.snapshot.paramMap.get('name')!;
    this.productService.getProduct(name)
      .subscribe((product) => this.product = product);
  }

  getReview(): void {
    const name = this.route.snapshot.paramMap.get('name')!;
    this.reviewService.getReviews(name)
      .subscribe((review: Review) => this.reviews = review);
  }

  getReviews(): string[] {
    return this.reviews.reviews;
  }

  goBack(): void {
    this.location.back();
  }

  addToCart(): void {
    this.cartService.addToCart(this.product);
    this.goBack();
  }

  inStock(): boolean {
    if(this.product){
      return this.product.quantity > 0;
    }

    return false;
  }

  isAdminLoggedIn(): boolean {
    var user: String;
    user = this.loginService.getCurrentUser();
    return !(user == null) && (user === "admin");
  } 

  addReview(): void {
    if (this.reviewToAdd) {
      var user: string;
      if (this.loginService.getCurrentUser() === '') {
        user = "Anon";
      } else {
        user = this.loginService.getCurrentUser();
      }
      if (this.reviews) {
        this.reviews.reviews[this.reviews.reviews.length] = user + ": " + this.reviewToAdd;
      } else {
        var newReview: string = user + ': ' + this.reviewToAdd;
        var newArray: string[] = [newReview];
      }
      this.reviewService.addReview(this.reviews).subscribe();
    }
  }

  save(): void {
    console.log("pee")
    if (this.product) {
      this.cartService.updatePrice(this.product);
      this.productService.updateProduct(this.product.name, this.product)
        .subscribe(() => this.goBack());
    }
  }

  saveWithName(originalName: string): void {
    this.status = "saveWithName";
    if (this.product) {
      this.status = "savingWithName";
      this.cartService.updatePrice(this.product);
      this.productService.updateProduct(originalName, this.product)
        .subscribe(() => this.goBack());
      this.status = "savedWithName";
    }
  }

  updateName(name: string): void {
    //this.getProduct();
    if (this.product) {
      var originalName = this.product.name;
      var curProduct = {"name":name, "price":this.product.price, "quantity":this.product.quantity, "image":this.product.image};
      this.product = curProduct;
      this.saveWithName(originalName);
    }
  }

  updatePrice(priceStr: string): void {
    //this.getProduct();
    if (this.product) {
      var price = +priceStr;
      var curProduct = {"name":this.product.name, "price":price, "quantity":this.product.quantity, "image":this.product.image};
      this.product = curProduct;
      this.save();
    }
  }

  updateQuantity(quantityStr: string): void {
    //this.getProduct();
    if (this.product) {
      var quantity = +quantityStr;
      var curProduct = {"name":this.product.name, "price":this.product.price, "quantity":quantity, "image":this.product.image};
      this.product = curProduct;
      this.save();
    }
  }

  getStatus(): string {
    return this.status;
  }
}