import { ComponentFixture, TestBed } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { OrdersComponent } from './orders.component';
import { OrderService } from '../../service/order.service';
import { of, throwError } from 'rxjs';

// Mock para listar pedidos
interface OrderList {
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

// Mock para ingresar pedidos
interface OrderInput {
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

describe('OrdersComponent', () => {
  let component: OrdersComponent;
  let fixture: ComponentFixture<OrdersComponent>;
  let orderService: jasmine.SpyObj<OrderService>;

  const mockOrderList: OrderList[] = [
    {
      id: 1,
      usuario: {
        id: 1,
        nombre: 'Usuario 1',
        correo: 'usuario1@example.com',
        telefono: '123456789',
        direccionEnvio: 'Dirección 1',
      },
      producto: {
        id: 1,
        nombre: 'Producto 1',
        precio: 50,
        descripcion: 'Desc 1',
        imagen: 'producto1.jpg',
      },
      cantidad: 2,
      fechaCompra: '2023-01-01',
      total: 100,
      estado: 'En Proceso',
    },
  ];

  const mockOrderInput: OrderInput = {
    id: 1,
    usuario: {
      id: 1,
      nombre: 'Usuario 1',
      correo: 'usuario1@example.com',
      telefono: '123456789',
      direccionEnvio: 'Dirección 1',
    },
    producto: {
      id: 1,
      nombre: 'Producto 1',
      precio: 50,
      descripcion: 'Desc 1',
      imagen: 'producto1.jpg',
    },
    cantidad: 2,
    fechaCompra: '2023-01-01',
    total: 100,
    estado: 'En Proceso',
  };

  beforeEach(async () => {
    const orderServiceMock = jasmine.createSpyObj('OrderService', [
      'getOrdersByUserId',
      'addOrder',
    ]);
    orderServiceMock.getOrdersByUserId.and.returnValue(of({ compras: mockOrderList }));
    orderServiceMock.addOrder.and.returnValue(of(mockOrderInput));

    await TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule, OrdersComponent],
      providers: [{ provide: OrderService, useValue: orderServiceMock }],
    }).compileComponents();

    fixture = TestBed.createComponent(OrdersComponent);
    component = fixture.componentInstance;
    orderService = TestBed.inject(OrderService) as jasmine.SpyObj<OrderService>;

    spyOn(localStorage, 'getItem').and.returnValue(
      JSON.stringify({ id: 1, nombre: 'Usuario 1', permisos: 'USER' })
    );

    fixture.detectChanges();
  });

  it('debería crear el componente', () => {
    expect(component).toBeTruthy();
  });

  it('debería cargar pedidos del usuario al inicializar', () => {
    expect(orderService.getOrdersByUserId).toHaveBeenCalledWith(1);
    expect(component.orders.length).toBe(1); // Debe cargar un pedido
    expect(component.pedidosFiltrados.length).toBe(1); // También debe inicializar pedidos filtrados
  });

  it('debería manejar el caso de no encontrar pedidos', () => {
    orderService.getOrdersByUserId.and.returnValue(of({ compras: [] }));
    component.ngOnInit();
    expect(component.orders.length).toBe(0);
    expect(component.pedidosFiltrados.length).toBe(0);
  });

  it('debería filtrar pedidos correctamente por el ID de usuario', () => {
    component.filtrarPedidos();
    expect(component.pedidosFiltrados.length).toBe(1);
    expect(component.pedidosFiltrados[0].id).toBe(1); // Solo un pedido debe coincidir con userId=1
  });

});
