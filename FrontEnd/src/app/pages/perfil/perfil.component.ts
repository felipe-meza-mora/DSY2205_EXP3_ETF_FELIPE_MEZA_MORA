import { Component, NgModule, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { ReactiveFormsModule, FormGroup, FormBuilder, Validators, AbstractControl, ValidationErrors } from '@angular/forms';
import { validarRut } from '../../validators/rut.validator';
import { UsersService } from '../../service/users.service';
import { User } from '../../models/users.model';

@Component({
  selector: 'app-perfil',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './perfil.component.html',
  styleUrl: './perfil.component.css'
})

/**
 * Componente para la gestión del perfil de usuario.
 * @description Este componente permite al usuario actualizar sus datos personales, incluyendo el nombre, email, contraseña, teléfono, permisos y dirección de envío.
 */

export class PerfilComponent implements OnInit {

   /**
   * FormGroup que contiene los controles del formulario de registro.
   * @type {FormGroup} Grupo de controles del formulario.
   */
  formRegistro!: FormGroup;
  
  /**
   * Mensaje de éxito mostrado al actualizar los datos del perfil.
   * @type {string} Mensaje que indica que los datos se actualizaron correctamente.
   */

  mensajeExito: string = '';

  constructor(private fb: FormBuilder, private router: Router, private userService : UsersService) {}

  /**
   * Método del ciclo de vida de Angular que se ejecuta al inicializar el componente.
   * Inicializa el formulario de registro con validadores y carga los datos del usuario si están disponibles en el almacenamiento local.
   */

  initialUserData: any;  // Para almacenar los datos iniciales

  ngOnInit(): void {
    const user = JSON.parse(localStorage.getItem('sesionUsuario') || '{}');
    console.log('Datos cargados en Home:', user); // Debug
    this.formRegistro = this.fb.group({
      rut: ['', [Validators.required, validarRut]],
      nombre: ['', Validators.required],
      correo: ['', [Validators.required, Validators.email]],
      password: ['', this.passwordValidator()],
      confirmPassword: [''],
      telefono: ['', Validators.required],
      permisos: [''],
      direccionEnvio: ['', Validators.required],
    }, { validators: this.passwordMatchValidator });

    this.loadUserData();
  }

  loadUserData(): void {
    const user = JSON.parse(localStorage.getItem('sesionUsuario') || '{}');
    if (user) {
      console.log('Datos cargados desde localStorage: ', user); // Debug
      this.initialUserData = { ...user };
  
      this.formRegistro.patchValue({
        rut: user.rut,
        nombre: user.nombre,
        correo: user.correo,
        telefono: user.telefono,
        permisos: user.permisos,
        direccionEnvio: user.direccionEnvio
      });
    }
  }

  hasChanges(): boolean {
    // Compara los valores actuales con los valores iniciales
    return JSON.stringify(this.formRegistro.value) !== JSON.stringify(this.initialUserData);
  }
   /**
   * Validador personalizado para la contraseña que verifica la complejidad de la misma.
   * @param control Control de formulario que contiene el valor de la contraseña.
   * @returns {ValidationErrors | null} Objeto de errores si la contraseña no cumple con los requisitos de complejidad, o null si es válida.
   */

  passwordValidator(): Validators | null {
    return (control: AbstractControl): ValidationErrors | null => {
      const value = control.value;
      const errors: any = {};

      if (value && !/[A-Z]/.test(value)) {
        errors.missingUppercase = 'Debe contener al menos una letra mayúscula';
      }
      if (value && !/[a-z]/.test(value)) {
        errors.missingLowercase = 'Debe contener al menos una letra minúscula';
      }
      if (value && !/[0-9]/.test(value)) {
        errors.missingNumber = 'Debe contener al menos un número';
      }
      if (value && !/[\W_]/.test(value)) {
        errors.missingSpecial = 'Debe contener al menos un carácter especial';
      }
      return Object.keys(errors).length ? errors : null;
    };
  }

   /**
   * Validador de coincidencia de contraseñas que verifica si la contraseña y su confirmación coinciden.
   * @param group FormGroup que contiene los campos de contraseña y confirmación.
   * @returns {ValidationErrors | null} Objeto de errores si las contraseñas no coinciden, o null si coinciden.
   */

  passwordMatchValidator(group: FormGroup): ValidationErrors | null {
    const password = group.get('password')?.value;
    const confirmPassword = group.get('confirmPassword')?.value;
    return password === confirmPassword ? null : { passwordMismatch: true };
  }

  /**
   * Método para enviar el formulario de registro.
   * Si el formulario es válido, actualiza los datos del usuario en el almacenamiento local y muestra un mensaje de éxito antes de redirigir al usuario.
   */
  

  submitForm(): void {
    if (this.formRegistro.valid) {
      const formData = { ...this.formRegistro.value };
      formData.id = this.initialUserData.id; // Asegúrate de incluir el ID
    
      console.log('Datos enviados al backend:', formData); // Debug
    
      this.userService.updateUser(formData.id, formData).subscribe({
        next: (response: any) => {
          this.mensajeExito = response.message;
      
          // Actualizar localStorage
          localStorage.setItem('sesionUsuario', JSON.stringify({ ...this.initialUserData, ...formData }));
          console.log('Datos en localStorage actualizados: ', JSON.parse(localStorage.getItem('sesionUsuario') || '{}'));
      
          // Recargar los datos del formulario
          this.loadUserData(); // Llama a tu método de carga de datos
          console.log('Formulario actualizado con los nuevos datos.');
      
          // Redirigir al usuario después de 4 segundos
          setTimeout(() => {
            this.router.navigate(['/home']);
          }, 4000);
        },
        error: (error) => {
          console.error('Error actualizando el perfil: ', error);
        }
      });
    } else {
      console.warn('El formulario no es válido:', this.formRegistro.value); // Debug
    }
  }

  /**
   * Método para limpiar el formulario de registro, restableciendo todos los campos a su estado inicial.
   */

  limpiarFormulario(): void {
    this.formRegistro.reset();
  }
}
