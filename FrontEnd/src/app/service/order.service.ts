import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { map, Observable } from 'rxjs';
import { Order } from '../models/order.model';
import { Order2 } from '../models/Order2';
@Injectable({
  providedIn: 'root'
})
export class OrderService {

  private apiUrl = 'http://localhost:8181/api/compras';  // URL de la API backend

  constructor(private http: HttpClient) {}

    /**
   * Obtiene los pedidos de un usuario por su ID.
   * @param usuarioId El ID del usuario.
   * @return {Observable<Order[]>} Los pedidos del usuario.
   */
    getOrdersByUserId(usuarioId: number): Observable<any> {
      return this.http.get<any>(`${this.apiUrl}/usuario/${usuarioId}`);
    }

  /**
   * Guarda un pedido en el backend.
   * @param order - Pedido a guardar.
   * @return {Observable<any>}
   */
  saveOrder(order: Order2): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/add`, order);
  }
   // Método para actualizar un pedido
   updateOrder(orderId: number, order: Order): Observable<Order> {
    return this.http.put<Order>(`${this.apiUrl}/${orderId}`, order);
  }

  /**
   * Obtiene todos los pedidos desde el backend.
   * @return {Observable<any>}
   */
   // Método para obtener todos los pedidos
   getAllOrders(): Observable<any> {
    return this.http.get<any>(this.apiUrl).pipe(
      map((response: { compras: any[] }) => response.compras) // Accede al arreglo de 'compras'
    );
  }

  getOrdersByEmail(email: string): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/email/${email}`);
  }

  /**
   * Elimina un pedido del backend por su ID.
   * @param id - ID del pedido.
   * @return {Observable<any>}
   */
  deleteOrder(id: number): Observable<any> {
    return this.http.delete<any>(`${this.apiUrl}/${id}`);
  }
}