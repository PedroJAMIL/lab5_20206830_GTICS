
package org.example.lab05_20206830.entity;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;

@Entity
@Table(name = "usuarios")
@Getter
@Setter
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(nullable = false)
    //@Size(max = 50,message = "Solo se soportan 40 caractéres")
    @NotBlank
    private String nombre;

    @Column(nullable = false)
    //@Size(max = 50,message = "Solo se soportan 40 caractéres")
    @NotBlank
    private String apellido;


    @Column(nullable = false)
    //@Size(max = 50,message = "Solo se soportan 40 caractéres")
    @NotBlank
    @Email
    private String correo;

    @Column(nullable = false)
    private int edad;

    @Column(nullable = false)
    //@Size(max = 50,message = "Solo se soportan 40 caractéres")
    @NotBlank
    private String descripcion;

    @Column(nullable = false)
    @Size(max = 15,message = "Solo se soportan 15 caractéres")
    @NotBlank
    private String contrasena;

}

