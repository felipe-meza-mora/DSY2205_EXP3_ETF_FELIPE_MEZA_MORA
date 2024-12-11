import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { User } from '../models/users.model';

@Injectable({
  providedIn: 'root'
})
export class UsersService {
  private apiUrl = 'http://localhost:8181/api/users'; // URL base del backend

  constructor(private http: HttpClient) {}

  // Método para añadir un nuevo usuario
  addUser(user: User): Observable<any> {
    return this.http.post(`${this.apiUrl}/register`, user, { responseType: 'text' as 'json' });
  }
  // Verifica si existe un usuario con el RUT proporcionado
  isRutRegistered(rut: string): Observable<boolean> {
    return this.http.post<boolean>(`${this.apiUrl}/check-rut`, { rut });
  }

  // Verifica si existe un usuario con el correo proporcionado
  isEmailRegistered(email: string): Observable<boolean> {
    return this.http.post<boolean>(`${this.apiUrl}/check-email`, { email });
  }

  // Cambia el tipo de retorno a Observable<User> y usa "correo" en lugar de "email"
  validateUser(correo: string, password: string): Observable<User> {
    return this.http.post<User>(`${this.apiUrl}/login`, { correo, password });
  }

  // Actualiza la contraseña de un usuario
  updatePassword(email: string, newPassword: string): Observable<void> {
    return this.http.put<void>(`${this.apiUrl}/update-password`, { email, newPassword });
  }

  // Actualiza los datos de un usuario (requiere el ID en la URL)
  updateUser(id: number, user: Partial<User>): Observable<User> {
    return this.http.put<User>(`${this.apiUrl}/${id}`, user);
  }

  // Obtiene todos los usuarios
  getUsers(): Observable<User[]> {
    return this.http.get<User[]>(`${this.apiUrl}`);
  }

  // Obtiene un usuario por su ID
  getUserById(id: number): Observable<User> {
    return this.http.get<User>(`${this.apiUrl}/${id}`);
  }

  // Elimina un usuario por ID
  deleteUser(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}