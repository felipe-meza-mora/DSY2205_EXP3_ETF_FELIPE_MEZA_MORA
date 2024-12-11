import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { SignUpComponent } from './sign-up.component';
import { UsersService } from '../../service/users.service';
import { of } from 'rxjs';

describe('SignUpComponent', () => {
  let component: SignUpComponent;
  let fixture: ComponentFixture<SignUpComponent>;
  let usersService: jasmine.SpyObj<UsersService>;

  beforeEach(async () => {
    const usersServiceMock = jasmine.createSpyObj('UsersService', [
      'isRutRegistered',
      'isEmailRegistered',
      'addUser'
    ]);
  
    await TestBed.configureTestingModule({
      imports: [
        SignUpComponent, // Mover a imports porque es un componente standalone
        ReactiveFormsModule,
        HttpClientTestingModule
      ],
      providers: [
        { provide: UsersService, useValue: usersServiceMock },
      ],
    }).compileComponents();
  
    fixture = TestBed.createComponent(SignUpComponent);
    component = fixture.componentInstance;
    usersService = TestBed.inject(UsersService) as jasmine.SpyObj<UsersService>;
    fixture.detectChanges();
  });

  it('Debería crear el componente', () => {
    expect(component).toBeTruthy();
  });

  it('Debería inicializar el formulario correctamente', () => {
    expect(component.formRegistro).toBeDefined();
    expect(component.formRegistro.controls['rut']).toBeDefined();
    expect(component.formRegistro.controls['nombre']).toBeDefined();
    expect(component.formRegistro.controls['email']).toBeDefined();
    expect(component.formRegistro.controls['password']).toBeDefined();
    expect(component.formRegistro.controls['confirmPassword']).toBeDefined();
    expect(component.formRegistro.controls['telefono']).toBeDefined();
    expect(component.formRegistro.controls['direccionEnvio']).toBeDefined();
  });

  it('Debería requerir que las contraseñas coincidan', () => {
    component.formRegistro.patchValue({
      password: 'Password123!',
      confirmPassword: 'DifferentPassword!',
    });
    expect(component.formRegistro.hasError('passwordMismatch')).toBeTruthy();
  });

  it('Debería requerir al menos una letra mayúscula en la contraseña', () => {
    component.formRegistro.controls['password'].setValue('password123!');
    const errors = component.formRegistro.controls['password'].errors;
    expect(errors?.['missingUppercase']).toBeTruthy();
  });

  it('Debería requerir al menos una letra minúscula en la contraseña', () => {
    component.formRegistro.controls['password'].setValue('PASSWORD123!');
    const errors = component.formRegistro.controls['password'].errors;
    expect(errors?.['missingLowercase']).toBeTruthy();
  });

  it('Debería requerir al menos un número en la contraseña', () => {
    component.formRegistro.controls['password'].setValue('Password!');
    const errors = component.formRegistro.controls['password'].errors;
    expect(errors?.['missingNumber']).toBeTruthy();
  });

  it('Debería marcar error si las contraseñas no coinciden', () => {
    component.formRegistro.patchValue({
      password: 'Password123!',
      confirmPassword: 'AnotherPassword!',
    });
  
    const errors = component.formRegistro.errors;
    expect(errors?.['passwordMismatch']).toBeTruthy();
  });

  it('Debería requerir al menos un carácter especial en la contraseña', () => {
    component.formRegistro.controls['password'].setValue('Password123');
    const errors = component.formRegistro.controls['password'].errors;
    expect(errors?.['missingSpecial']).toBeTruthy();
  });



});
