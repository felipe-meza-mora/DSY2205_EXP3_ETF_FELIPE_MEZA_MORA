import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { RouterTestingModule } from '@angular/router/testing';
import { PerfilComponent } from './perfil.component';
import { UsersService } from '../../service/users.service';
import { of, throwError } from 'rxjs';
import { User } from '../../models/users.model';

describe('PerfilComponent', () => {
  let component: PerfilComponent;
  let fixture: ComponentFixture<PerfilComponent>;
  let usersService: jasmine.SpyObj<UsersService>;

  beforeEach(async () => {
    const usersServiceMock = jasmine.createSpyObj('UsersService', ['updateUser']);

    await TestBed.configureTestingModule({
      imports: [
        PerfilComponent, // Agregar el componente standalone aquí
        ReactiveFormsModule,
        RouterTestingModule,
      ],
      providers: [
        { provide: UsersService, useValue: usersServiceMock },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(PerfilComponent);
    component = fixture.componentInstance;
    usersService = TestBed.inject(UsersService) as jasmine.SpyObj<UsersService>;
    fixture.detectChanges();
  });

  it('Debería crear el componente', () => {
    expect(component).toBeTruthy();
  });

  it('Debería inicializar el formulario con los campos correctos', () => {
    const form = component.formRegistro;
    expect(form).toBeDefined();
    expect(form.controls['rut']).toBeDefined();
    expect(form.controls['nombre']).toBeDefined();
    expect(form.controls['correo']).toBeDefined();
    expect(form.controls['password']).toBeDefined();
    expect(form.controls['confirmPassword']).toBeDefined();
    expect(form.controls['telefono']).toBeDefined();
    expect(form.controls['permisos']).toBeDefined();
    expect(form.controls['direccionEnvio']).toBeDefined();
  });

  it('Debería cargar los datos del usuario desde localStorage', () => {
    const mockUser = {
      id: 1,
      rut: '12345678-9',
      nombre: 'Test User',
      correo: 'test@example.com',
      telefono: '123456789',
      permisos: 'USER',
      direccionEnvio: 'Test Address',
    };

    localStorage.setItem('sesionUsuario', JSON.stringify(mockUser));

    component.loadUserData();
    expect(component.formRegistro.value).toEqual({
      rut: '12345678-9',
      nombre: 'Test User',
      correo: 'test@example.com',
      password: '',
      confirmPassword: '',
      telefono: '123456789',
      permisos: 'USER',
      direccionEnvio: 'Test Address',
    });
  });

  it('Debería detectar cambios en el formulario', () => {
    const initialData = {
      rut: '12345678-9',
      nombre: 'Test User',
      correo: 'test@example.com',
      password: '',
      confirmPassword: '',
      telefono: '123456789',
      permisos: 'USER',
      direccionEnvio: 'Test Address',
    };

    component.initialUserData = { ...initialData };
    component.formRegistro.patchValue({ nombre: 'Nuevo Nombre' });

    expect(component.hasChanges()).toBeTrue();
  });


});
