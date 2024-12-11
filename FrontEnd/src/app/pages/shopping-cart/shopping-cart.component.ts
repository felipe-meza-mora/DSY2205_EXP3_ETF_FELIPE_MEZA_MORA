import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { Product } from '../../models/product.model';
import { ProductService } from '../../service/product.service';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { OrderService } from '../../service/order.service';
import { Order } from '../../models/order.model';
import { Order2 } from '../../models/Order2';

declare var bootstrap: any;

@Component({
  selector: 'app-shopping-cart',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './shopping-cart.component.html',
  styleUrls: ['./shopping-cart.component.css']
})

export class ShoppingCartComponent implements OnInit {

  cart: { product: Product, quantity: number }[] = [];
  total: number = 0;
  userLoggedIn: boolean = false;
  userInfoForm: FormGroup;
  cartQuantity: number = 0;

  constructor(
    private productService: ProductService, 
    private fb: FormBuilder, 
    private router: Router, 
    private orderService: OrderService,
    private cdr: ChangeDetectorRef // Para forzar la actualización de la vista
  ) {
    this.userInfoForm = this.fb.group({
      nombre: ['', Validators.required],
      direccion: ['', Validators.required],
      correo: ['', [Validators.required, Validators.email]],
      telefono: ['', Validators.required],
    });
  }

  ngOnInit(): void {
    this.loadCart();
    this.checkUserSession();
  }

  private loadCart(): void {
    this.cart = this.productService.getCart();
    this.calculateTotal();
  }

  private calculateTotal(): void {
    this.total = this.cart.reduce((sum, item) => sum + item.product.precio * item.quantity, 0);
  }

  private checkUserSession(): void {
    const sesionUsuario = localStorage.getItem('sesionUsuario');
    this.userLoggedIn = !!sesionUsuario;
    if (this.userLoggedIn && sesionUsuario) {
      const userData = JSON.parse(sesionUsuario);
      this.userInfoForm.patchValue({
        nombre: userData.nombre,
        direccion: userData.direccionEnvio,
        correo: userData.email,
        telefono: userData.telefono,
      });
    }
  }

  incrementQuantity(productId: number): void {
    this.productService.incrementQuantity(productId);
    this.loadCart();
  }

  decrementQuantity(productId: number): void {
    this.productService.decrementQuantity(productId);
    this.loadCart();
  }
   
  clearCart(): void {
    this.productService.clearCart();
    this.loadCart();
  }


  private showToast(message: string): void {
    const toastElement = document.getElementById('liveToast');
    const toastBodyElement = document.getElementById('toast-body');
  
    if (toastBodyElement) {
      toastBodyElement.innerText = message;
    }
  
    if (toastElement) {
      const toast = new bootstrap.Toast(toastElement);
      toast.show(); // Esto mostrará el toast
    }
  }
  
  proceedToCheckout(): void {
    const userData = JSON.parse(localStorage.getItem('sesionUsuario') || '{}');
    
    // Recorremos el carrito y enviamos una compra a la vez
    let successfulPurchases = 0; // Para contar cuántas compras fueron exitosas
  
    this.cart.forEach((item, index) => {
      const orderPayload: any = {
        usuario: { id: userData.id },  // El ID del usuario
        producto: { id: item.product.id },  // El ID del producto
        cantidad: item.quantity,    // La cantidad seleccionada
        total: item.product.precio * item.quantity,  // Subtotal del producto
        fechaCompra: new Date().toISOString(),  // Fecha actual para la compra
        estado: 'Pendiente' // Estado de la compra
      };
  
      // Enviar cada producto de forma individual al backend
      this.orderService.saveOrder(orderPayload).subscribe({
        next: (response) => {
          console.log('Producto guardado con éxito:', response);
          successfulPurchases++;
  
          // Si es el último producto y todo ha salido bien, vaciar el carrito
          if (successfulPurchases === this.cart.length) {
            this.clearCart();
            this.showToast('Compra realizada con éxito!');  // Mostrar el toast
            this.router.navigate(['/confirmation']);
          }
        },
        error: (err) => {
          console.error('Error al realizar la compra para el producto:', err);
          this.showToast('Error al realizar la compra.');
        }
      });
    });
  }
}
