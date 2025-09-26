package org.example.lab05_20206830.entity;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;

public class Mensaje {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @NotBlank
    @Column(name = "remitente_id",nullable = false)
    private int remitente_id;

    @NotBlank
    @Column(name = "destinatario_id",nullable = false)
    private int destinatario_id;

    @NotBlank
    @Column(name = "regalo_tipo",nullable = false)
    private boolean regalo_tipo;

    @NotBlank
    @Column(name = "regalo_color",nullable = false)
    private String regalo_color;

    @NotBlank
    @Column(name = "contenido",nullable = false)
    private String contenido;

    @NotBlank
    @Column(name = "fecha_envio",nullable = false)
    private String fecha_envio;
}
