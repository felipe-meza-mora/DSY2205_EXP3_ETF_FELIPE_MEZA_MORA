import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, BehaviorSubject } from 'rxjs';
import { Product } from '../models/product.model';

@Injectable({
  providedIn: 'root'
})
export class ProductService {
  private apiUrl = 'http://localhost:8181/api/products'; // URL base del backend

  // BehaviorSubject para manejar el carrito de compras
  private cart = new BehaviorSubject<{ product: Product, quantity: number }[]>(this.getCart());
  cart$ = this.cart.asObservable();

  constructor(private http: HttpClient) {}

  // Método para verificar si existe un producto por ID
  checkProductIdExists(id: number): Observable<boolean> {
    return this.http.get<boolean>(`${this.apiUrl}/${id}`);
  }

  // Método para obtener un producto por ID
  getProductById(id: number): Observable<Product> {
    return this.http.get<Product>(`${this.apiUrl}/${id}`);
  }

    // ProductService (Asegúrate de que esta función esté en tu servicio)
  addProduct(product: Product): Observable<Product> {
    return this.http.post<Product>(`${this.apiUrl}/add`, product); // Usando tu API de backend para agregar el producto
  }

   // Método para actualizar un producto
   updateProduct(id: number, product: Partial<Product>): Observable<Product> {
    return this.http.put<Product>(`${this.apiUrl}/${id}`, product);
  }

   // Método para eliminar un producto
   deleteProduct(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }


  // Método para agregar un producto al carrito de compras
  addToCart(product: Product): void {
    let cart = this.getCart();
    const existingProduct = cart.find(item => item.product.id === product.id);

    if (existingProduct) {
      // Si el producto ya existe, incrementamos su cantidad
      existingProduct.quantity += 1;
    } else {
      // Si el producto no existe, lo agregamos al carrito
      cart.push({ product, quantity: 1 });
    }

    // Actualizamos el carrito en localStorage
    this.updateCart(cart);
  }

  // Método para obtener todos los productos
  getProducts(): Observable<{ products: Product[] }> {
    return this.http.get<{ products: Product[] }>(`${this.apiUrl}`);
  }

  // Método para incrementar la cantidad de un producto en el carrito
  incrementQuantity(productId: number): void {
    let cart = this.getCart();
    const product = cart.find(item => item.product.id === productId);

    if (product) {
      product.quantity += 1;
      this.updateCart(cart);
    }
  }

  // Método para decrementar la cantidad de un producto en el carrito
  decrementQuantity(productId: number): void {
    let cart = this.getCart();
    const product = cart.find(item => item.product.id === productId);

    if (product && product.quantity > 1) {
      product.quantity -= 1;
      this.updateCart(cart);
    }
  }

  // Método para obtener el carrito de compras desde localStorage
  getCart(): { product: Product, quantity: number }[] {
    const cart = localStorage.getItem('cart');
    return cart ? JSON.parse(cart) : [];
  }
  // Método para eliminar un producto del carrito
  removeFromCart(productId: number): void {
    let cart = this.getCart();
    cart = cart.filter(item => item.product.id !== productId);
    this.updateCart(cart);
  }

  // Método para limpiar el carrito
  clearCart(): void {
    localStorage.removeItem('cart');
    this.cart.next([]); // Notificar a los suscriptores que el carrito está vacío
  }

  
  // Método para actualizar el carrito en localStorage
  updateCart(cart: { product: Product, quantity: number }[]): void {
    localStorage.setItem('cart', JSON.stringify(cart));
    this.cart.next(cart); // Notificar a los suscriptores del cambio
  }
}
