export interface Order {
  id: number;
  usuario: {
    id: number;
    nombre: string;
    correo: string;
    telefono: string;
    direccionEnvio: string;
  };
  producto: {
    id: number;
    nombre: string;
    precio: number;
    descripcion: string;
    imagen: string;
  };
  cantidad: number;
  fechaCompra: string;  
  total: number | null;  
  estado: string | null;  
}