export interface User {
  id: number;
  nombre: string;
  password: string;
  permisos: string;
  rut: string;
  correo: string;
  telefono?: string;
  direccionEnvio: string;
}