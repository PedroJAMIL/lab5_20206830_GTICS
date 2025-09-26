package org.example.lab05_20206830.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "mensajes")
@Getter
@Setter
public class Mensaje {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "remitente_id", nullable = false)
    private Usuario remitente;

    @ManyToOne
    @JoinColumn(name = "destinatario_id", nullable = false)
    private Usuario destinatario;

    @NotBlank(message = "Debe seleccionar un tipo de regalo")
    @Column(name = "regalo_tipo", nullable = false)
    private String regaloTipo; // "flor" o "carrito"

    @Column(name = "regalo_color")
    private String regaloColor; // Solo para carrito, null para flor amarilla

    @NotBlank(message = "El mensaje no puede estar vacío")
    @Size(min = 20, message = "El mensaje debe tener al menos 20 caracteres")
    @Pattern(regexp = "^(?!.*(odio|feo)).*$", 
             message = "El mensaje no debe contener las palabras 'odio' ni 'feo'",
             flags = Pattern.Flag.CASE_INSENSITIVE)
    @Column(name = "contenido", nullable = false, columnDefinition = "TEXT")
    private String contenido;

    @Column(name = "fecha_envio", nullable = false)
    private LocalDateTime fechaEnvio = LocalDateTime.now();

    // Constructor vacío
    public Mensaje() {}

    // Constructor con parámetros
    public Mensaje(Usuario remitente, Usuario destinatario, String regaloTipo, 
                   String regaloColor, String contenido) {
        this.remitente = remitente;
        this.destinatario = destinatario;
        this.regaloTipo = regaloTipo;
        this.regaloColor = regaloColor;
        this.contenido = contenido;
        this.fechaEnvio = LocalDateTime.now();
    }

    // Métodos setters explícitos para compatibilidad con el controlador
    public void setRemitente(Usuario remitente) {
        this.remitente = remitente;
    }

    public void setDestinatario(Usuario destinatario) {
        this.destinatario = destinatario;
    }

    public void setRegaloTipo(String regaloTipo) {
        this.regaloTipo = regaloTipo;
    }

    public void setRegaloColor(String regaloColor) {
        this.regaloColor = regaloColor;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }
}
