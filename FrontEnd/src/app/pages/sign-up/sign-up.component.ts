import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators, AbstractControl, ValidationErrors } from '@angular/forms';
import { UsersService } from '../../service/users.service';
import { User } from '../../models/users.model';
import { validarRut } from '../../validators/rut.validator';

@Component({
  selector: 'app-sign-up',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './sign-up.component.html',
  styleUrls: ['./sign-up.component.css']
})
export class SignUpComponent implements OnInit {
  formRegistro!: FormGroup;
  mensajeExito: string | null = null;
  mensajeError: string | null = null;

  constructor(private fb: FormBuilder, private usersService: UsersService) {}

  ngOnInit(): void {
    this.formRegistro = this.fb.group({
      rut: ['', [Validators.required, validarRut]],
      nombre: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [
        Validators.required,
        Validators.minLength(8),
        Validators.maxLength(20),
        this.passwordStrengthValidator
      ]],
      confirmPassword: ['', Validators.required],
      telefono: ['', [Validators.required, Validators.pattern('[0-9]+')]],
      direccionEnvio: ['', Validators.required]
    }, { validator: this.passwordMatchValidator });
  }

  passwordMatchValidator(group: AbstractControl): ValidationErrors | null {
    const password = group.get('password');
    const confirmPassword = group.get('confirmPassword');
    if (password && confirmPassword && password.value !== confirmPassword.value) {
      return { passwordMismatch: true };
    }
    return null;
  }

  passwordStrengthValidator(control: AbstractControl): ValidationErrors | null {
    const value = control.value;
    if (!value) return null;

    const errors: any = {};
    if (!/[A-Z]/.test(value)) errors.missingUppercase = 'La contraseña debe contener al menos una letra mayúscula';
    if (!/[a-z]/.test(value)) errors.missingLowercase = 'La contraseña debe contener al menos una letra minúscula';
    if (!/[0-9]/.test(value)) errors.missingNumber = 'La contraseña debe contener al menos un número';
    if (!/[@$!%*?&]/.test(value)) errors.missingSpecial = 'La contraseña debe contener al menos un carácter especial (&#64;$!%*?)';

    return Object.keys(errors).length ? errors : null;
  }

  async submitForm(): Promise<void> {
    if (this.formRegistro.valid) {
      const rut = this.formRegistro.get('rut')?.value;
      const email = this.formRegistro.get('email')?.value;

      try {
        const rutRegistered = await this.usersService.isRutRegistered(rut).toPromise();
        if (rutRegistered) {
          this.mensajeError = 'El RUT ingresado ya está registrado.';
          setTimeout(() => {
            this.formRegistro.reset();
            this.mensajeError = null;
          }, 3000);
          return;
        }

        const emailRegistered = await this.usersService.isEmailRegistered(email).toPromise();
        if (emailRegistered) {
          this.mensajeError = 'El correo electrónico ingresado ya está registrado.';
          setTimeout(() => {
            this.formRegistro.reset();
            this.mensajeError = null;
          }, 3000);
          return;
        }

        const nuevoUsuario: User = {
          id: 0, 
          rut: this.formRegistro.get('rut')?.value,
          nombre: this.formRegistro.get('nombre')?.value,
          correo: this.formRegistro.get('email')?.value, 
          password: this.formRegistro.get('password')?.value,
          telefono: this.formRegistro.get('telefono')?.value,
          direccionEnvio: this.formRegistro.get('direccionEnvio')?.value,
          permisos: 'USER',
        };

        await this.usersService.addUser(nuevoUsuario).toPromise();
        const nombreUsuario = this.formRegistro.get('nombre')?.value;
        this.mensajeExito = `¡${nombreUsuario}, tu información ha sido guardada exitosamente!`;

        setTimeout(() => {
          this.formRegistro.reset();
          this.mensajeExito = null;
        }, 3000);

      } catch (error) {
        console.error("Error al registrar el usuario: ", error);
      }
    }
  }

  limpiarFormulario() {
    this.formRegistro.reset();
  }
}
