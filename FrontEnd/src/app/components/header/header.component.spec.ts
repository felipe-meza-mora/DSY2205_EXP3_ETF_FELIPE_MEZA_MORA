import { TestBed } from '@angular/core/testing';
import { HeaderComponent } from './header.component';
import { ProductService } from '../../service/product.service';
import { UserSessionService } from '../../service/user-session.service';
import { Router } from '@angular/router';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, BehaviorSubject } from 'rxjs';
import { Product } from '../../models/product.model';

describe('HeaderComponent', () => {
  let component: HeaderComponent;
  let fixture: any;
  let mockProductService: jasmine.SpyObj<ProductService>;
  let mockUserSessionService: jasmine.SpyObj<UserSessionService>;

  beforeEach(async () => {
    // Mock para ProductService
    mockProductService = jasmine.createSpyObj('ProductService', ['cart$', 'addToCart']);
    mockProductService.cart$ = new BehaviorSubject([
      { product: { id: 1, nombre: 'Producto 1', precio: 100, descripcion: 'Desc 1', imagen: 'img1.jpg' }, quantity: 3 }
    ]).asObservable();

    // Mock para UserSessionService
    mockUserSessionService = jasmine.createSpyObj('UserSessionService', ['user$', 'clearUser']);
    mockUserSessionService.user$ = new BehaviorSubject<any>({ nombre: 'Test User', permisos: 'admin' }).asObservable();

    // Mock para ActivatedRoute
    const mockActivatedRoute = {
      snapshot: {
        params: {},
        queryParams: {}
      }
    };

    await TestBed.configureTestingModule({
      imports: [
        HeaderComponent, // HeaderComponent como standalone
        RouterTestingModule // Módulo para pruebas de rutas
      ],
      providers: [
        { provide: ProductService, useValue: mockProductService },
        { provide: UserSessionService, useValue: mockUserSessionService },
        { provide: ActivatedRoute, useValue: mockActivatedRoute }
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(HeaderComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create the component', () => {
    expect(component).toBeTruthy();
  });

  // Más pruebas aquí...
});
