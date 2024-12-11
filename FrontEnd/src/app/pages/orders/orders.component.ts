import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { OrderService } from '../../service/order.service';
import { ActivatedRoute } from '@angular/router';
import { Order } from '../../models/order.model';

@Component({
  selector: 'app-orders',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './orders.component.html',
  styleUrls: ['./orders.component.css']
})
export class OrdersComponent implements OnInit {

  pedidosFiltrados: Order[] = [];  // Aquí almacenaremos los pedidos filtrados por ID
  orders: Order[] = [];
  userId: number;  // Almacenamos el ID del usuario

  constructor(private orderService: OrderService, private route: ActivatedRoute) {
    this.userId = 0;  // Inicializamos el userId a 0
  }

  ngOnInit(): void {
    // Obtenemos el userId desde localStorage o URL
    const userData = localStorage.getItem('sesionUsuario');
    if (userData) {
      const parsedUserData = JSON.parse(userData);
      this.userId = parsedUserData.id;  // Asignamos el userId obtenido desde localStorage

      // Llamamos al servicio para obtener los pedidos del usuario por ID
      this.orderService.getOrdersByUserId(this.userId).subscribe({
        next: (response) => {
          this.orders = response.compras;  // Asignamos la lista de compras al array orders
          this.pedidosFiltrados = this.orders;  // Inicializamos pedidosFiltrados con todos los pedidos del usuario
        },
        error: (err) => {
          console.error('Error al obtener los pedidos:', err);
        }
      });
    }
  }

  // Método para filtrar los pedidos por el ID de usuario
  filtrarPedidos() {
    // Si el correo está vacío, filtramos con el userId directamente
    if (this.userId) {
      this.pedidosFiltrados = this.orders.filter(pedido => pedido.usuario.id === this.userId);
    } else {
      this.pedidosFiltrados = [];
    }
  }
}
