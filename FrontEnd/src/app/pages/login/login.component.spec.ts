import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { RouterTestingModule } from '@angular/router/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { LoginComponent } from './login.component';
import { UsersService } from '../../service/users.service';
import { UserSessionService } from '../../service/user-session.service';
import { of } from 'rxjs';
import { User } from '../../models/users.model';

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  let usersService: jasmine.SpyObj<UsersService>;

  beforeEach(async () => {
    const usersServiceMock = jasmine.createSpyObj('UsersService', ['validateUser']);
    const userSessionServiceMock = jasmine.createSpyObj('UserSessionService', ['setUser']);

    await TestBed.configureTestingModule({
      imports: [
        ReactiveFormsModule,
        RouterTestingModule,
        HttpClientTestingModule,
        LoginComponent // Agregado aquí como un import
      ],
      providers: [
        { provide: UsersService, useValue: usersServiceMock },
        { provide: UserSessionService, useValue: userSessionServiceMock },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    usersService = TestBed.inject(UsersService) as jasmine.SpyObj<UsersService>;
    fixture.detectChanges();
  });

  it('debería crear el componente de login', () => {
    expect(component).toBeTruthy();
  });

  it('debería validar las credenciales del usuario', async () => {
    const mockUser = {
      id: 1,
      nombre: 'Test User',
      password: 'password123',
      permisos: 'admin',
      rut: '12345678-9',
      correo: 'test@example.com',
      telefono: '123456789',
      direccionEnvio: 'Test Address',
    };

    usersService.validateUser.and.returnValue(of(mockUser));

    component.formLogin.setValue({ email: 'test@example.com', password: 'password123' });
    await component.onSubmit();

    expect(usersService.validateUser).toHaveBeenCalledWith('test@example.com', 'password123');
    expect(localStorage.getItem('sesionUsuario')).toBe(JSON.stringify(mockUser));
  });

  it('debería mostrar un mensaje de error si la contraseña es incorrecta', async () => {
    usersService.validateUser.and.returnValue(of(null as unknown as User));
  
    component.formLogin.setValue({ email: 'test@example.com', password: 'wrongpassword' });
    await component.onSubmit();
  
    expect(component.mensajeError).toBe('La contraseña ingresada es incorrecta');
  });
  
});
