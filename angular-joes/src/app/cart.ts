import { Product } from './product';
import { ProductComponent } from './product/product.component';
import { ProductService } from './product.service';

export class Cart{
    private items: Product[];

    constructor (items: Product[]) {
        this.items = items;
    }

    setItems (products: Product[]) {
        this.items = products;
    }

    addItem (p: Product) {
        var index = this.findItem(p.name);
        if (index != -1) { // item already in cart. increase quantity
            this.items[index].quantity++;
        } else { // new item
            this.items[this.items.length] = p;
            this.items[this.items.length-1].quantity = 1;
        }
    }

    clearItems () {
        this.items = [];
    }

    increaseQuantity (p: Product) {
        var index = this.findItem(p.name);
        this.items[index].quantity++;
    }

    decreaseQuantity (p: Product) {
        var index = this.findItem(p.name);
        if (this.items[index].quantity != 1) {
            this.items[index].quantity--;
        }
        else {
            this.remove(p);
        }
    }

    remove (p: Product) {
        var index = this.findItem(p.name);
        this.items.splice(index, 1);
    }

    getQuantity (p: Product): number {
        var index = this.findItem(p.name);
        return this.items[index].quantity;
    }

    getPrice (p: Product): number {
        var index = this.findItem(p.name);
        return this.items[index].price;
    }

    updateProductPrice (p: Product, newPrice: number): void {
        var index = this.findItem(p.name);
        this.items[index].price = newPrice;
    }

    getItems () {
        return this.items;
    }

    findItem (s: String): number {
        for (var i = 0; i < this.items.length; i++) {
            if (this.items[i].name === s) {
                return i;
            }
        }
        return -1;
    }

    getTotal (): number {
        let total: number;
        total = 0;
        for (var i = 0; i < this.items.length; i++) {
            total += (this.items[i].price * this.items[i].quantity);
        }
        return total;
    }

    //the cart I'm using in loginService is just an array of products, because it works just as fine,
    //Products already have a quantity value, cartSize is just this.cart.length
    getAsArray(): Product[] {
        //var productSet: Set<Product>;
        var productRet: Product[] = [];
        /*
        this.items.forEach(element => {
            var curProduct: Product;
            curProduct = {"name":element.name, "price":element.price, "quantity":this.findItem(element.name)}
            if (!productSet.has(curProduct)) {
                productSet.add(curProduct);
                productRet.push(curProduct);
            }
        });
        */
        return productRet;
    }
}