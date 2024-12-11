import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { ReactiveFormsModule, FormGroup, FormBuilder, Validators, AbstractControl, ValidationErrors } from '@angular/forms';
import { UsersService } from '../../service/users.service';
import { UserSessionService } from '../../service/user-session.service';
import { User } from '../../models/users.model'; 

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})

/**
 * Componente para la autenticación de usuarios mediante correo electrónico y contraseña.
 * @description Este componente permite a los usuarios iniciar sesión verificando las credenciales con los datos almacenados en el local storage.
 */
export class LoginComponent implements OnInit {

  formLogin!: FormGroup;
  mensajeError: string | null = null;
  correoNoRegistrado = false;
  
  // Nueva variable para controlar si el formulario fue enviado
  submitted = false;

  constructor(private fb: FormBuilder, private router: Router, private usersService: UsersService, private userSessionService: UserSessionService) {}

  ngOnInit(): void {
    this.formLogin = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required]
    });
  }

  async onSubmit(): Promise<void> {
    this.submitted = true; // Marca que el formulario fue enviado

    if (this.formLogin.invalid) {
      return; // Si el formulario es inválido, no hacer nada
    }

    const email = this.formLogin.get('email')?.value;
    const password = this.formLogin.get('password')?.value;

    try {
      const usuario = await this.usersService.validateUser(email, password).toPromise();
      if (usuario) {
        const userData: User = {
          id: usuario.id,
          nombre: usuario.nombre,
          password: usuario.password,
          permisos: usuario.permisos,
          rut: usuario.rut,
          correo: usuario.correo,
          telefono: usuario.telefono,
          direccionEnvio: usuario.direccionEnvio
        };
        localStorage.setItem('sesionUsuario', JSON.stringify(userData));
        this.userSessionService.setUser(userData);
        this.router.navigate(['/']);
        this.mensajeError = null;
        this.correoNoRegistrado = false;
      } else {
        this.mensajeError = 'La contraseña ingresada es incorrecta';
        this.correoNoRegistrado = true;
      }
    } catch (error) {
      console.error("Error al iniciar sesión: ", error);
      this.mensajeError = 'Hubo un error al iniciar sesión. Por favor, inténtelo de nuevo más tarde.';
    }
  }
}