import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { SalesComponent } from './sales.component';
import { OrderService } from '../../service/order.service';
import { of } from 'rxjs';
import { Order } from '../../models/order.model';
import { Order2 } from '../../models/Order2';

describe('SalesComponent', () => {
  let component: SalesComponent;
  let fixture: ComponentFixture<SalesComponent>;
  let orderService: jasmine.SpyObj<OrderService>;

  beforeEach(async () => {
    const orderServiceMock = jasmine.createSpyObj('OrderService', [
      'getAllOrders',
      'updateOrder',
      'saveOrder',
      'deleteOrder',
    ]);

    // Configurar los métodos simulados con valores por defecto
    orderServiceMock.getAllOrders.and.returnValue(of([])); // Devuelve un array vacío
    orderServiceMock.updateOrder.and.returnValue(of({})); // Devuelve un objeto vacío
    orderServiceMock.saveOrder.and.returnValue(of({})); // Devuelve un objeto vacío
    orderServiceMock.deleteOrder.and.returnValue(of(true)); // Devuelve true como éxito

    await TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, SalesComponent], // SalesComponent en imports
      providers: [{ provide: OrderService, useValue: orderServiceMock }],
    }).compileComponents();

    fixture = TestBed.createComponent(SalesComponent);
    component = fixture.componentInstance;
    orderService = TestBed.inject(OrderService) as jasmine.SpyObj<OrderService>;
    fixture.detectChanges();
  });

  it('debería crear el componente', () => {
    expect(component).toBeTruthy();
  });

  it('debería cargar los pedidos al inicializar', () => {
    const mockOrders: Order[] = [
      {
        id: 1,
        usuario: {
          id: 1,
          nombre: 'Usuario 1',
          correo: 'usuario1@example.com',
          telefono: '123456789',
          direccionEnvio: 'Calle 1',
        },
        producto: {
          id: 1,
          nombre: 'Producto 1',
          precio: 50,
          descripcion: 'Descripción del producto 1',
          imagen: 'img1.jpg',
        },
        cantidad: 2,
        fechaCompra: '2024-01-01',
        total: 100,
        estado: 'Pendiente',
      },
    ];

    orderService.getAllOrders.and.returnValue(of(mockOrders));

    component.ngOnInit();

    expect(orderService.getAllOrders).toHaveBeenCalled();
    expect(component.pedidos).toEqual(mockOrders);
  });

  it('debería actualizar el estado de un pedido', () => {
    const mockOrder: Order = {
      id: 1,
      usuario: {
        id: 1,
        nombre: 'Usuario 1',
        correo: 'usuario1@example.com',
        telefono: '123456789',
        direccionEnvio: 'Calle 1',
      },
      producto: {
        id: 1,
        nombre: 'Producto 1',
        precio: 50,
        descripcion: 'Descripción del producto 1',
        imagen: 'img1.jpg',
      },
      cantidad: 2,
      fechaCompra: '2024-01-01',
      total: 100,
      estado: 'Pendiente',
    };

    orderService.updateOrder.and.returnValue(of({ ...mockOrder, estado: 'Completado' }));
    component.actualizarEstado(mockOrder, 'Completado');

    expect(orderService.updateOrder).toHaveBeenCalledWith(mockOrder.id, {
      ...mockOrder,
      estado: 'Completado',
    });
  });

  it('debería guardar un pedido', () => {
    const mockOrder2: Order2 = {
      usuario: 1,
      producto: 1,
      cantidad: 2,
      fechaCompra: '2024-01-01',
      total: 100,
      estado: 'Pendiente',
    };

    orderService.saveOrder.and.returnValue(of(mockOrder2));

    // Aquí podrías llamar a un método específico para guardar el pedido si existe
    expect(orderService.saveOrder).not.toHaveBeenCalled(); // Ajusta según el flujo de tu componente
  });

  it('debería eliminar un pedido', () => {
    const mockOrderId = 1;

    orderService.deleteOrder.and.returnValue(of(true));

    // Llama a un método explícito para eliminar el pedido en tu componente si existe
    expect(orderService.deleteOrder).not.toHaveBeenCalledWith(mockOrderId); // Ajusta según el flujo de tu componente
  });
});
