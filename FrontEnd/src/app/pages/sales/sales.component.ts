import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { OrderService } from '../../service/order.service';
import { Order } from '../../models/order.model';

declare var bootstrap: any;

@Component({
  selector: 'app-sales',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './sales.component.html',
  styleUrls: ['./sales.component.css']
})
export class SalesComponent implements OnInit {

  pedidos: Order[] = []; // Aquí almacenaremos los pedidos
  usuario: any = null;
  permisos: string = '';

  constructor(private orderService: OrderService) {}

  ngOnInit(): void {
    // Obtener el usuario desde el localStorage
    const usuarioStorage = localStorage.getItem('usuario');
    if (usuarioStorage) {
      this.usuario = JSON.parse(usuarioStorage);
      this.permisos = this.usuario.permisos;
    }

    // Obtener los pedidos de Firestore usando el servicio
    this.orderService.getAllOrders().subscribe({
      next: (orders: Order[]) => {
        this.pedidos = orders; // Asignar los pedidos al arreglo
      },
      error: (error) => {
        console.error('Error al cargar los pedidos:', error);
      }
    });
  }

  // Método para actualizar el estado de un pedido
  actualizarEstado(pedido: Order, nuevoEstado: string): void {
    pedido.estado = nuevoEstado;
    
    this.orderService.updateOrder(pedido.id, pedido).subscribe({
      next: (updatedPedido) => {
        this.showToast('Pedido actualizado con éxito');
      },
      error: (error) => {
        console.error('Error actualizando el pedido:', error);
        this.showToast('Error al actualizar el pedido');
      }
    });
  }
  

  private showToast(message: string): void {
    const toastElement = document.getElementById('liveToast');
    const toastBodyElement = document.getElementById('toast-body');
  
    if (toastBodyElement) {
      toastBodyElement.innerText = message;
    }
  
    if (toastElement) {
      const toast = new bootstrap.Toast(toastElement);
      toast.show();
    }
  }
  
}