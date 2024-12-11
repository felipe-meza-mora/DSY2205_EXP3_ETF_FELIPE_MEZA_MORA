export interface Order2 {
    usuario: number;  // El ID del usuario
    producto: number; // El ID del producto
    cantidad: number;
    fechaCompra: string; // Fecha de la compra
    total: number;  // El total calculado
    estado: string;  // El estado inicial de la compra
  }