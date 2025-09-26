package org.example.lab05_20206830.repository;

import org.example.lab05_20206830.entity.Mensaje;
import org.example.lab05_20206830.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MensajeRepository extends JpaRepository<Mensaje, Integer> {
    
    // Buscar mensajes por destinatario
    List<Mensaje> findByDestinatarioOrderByFechaEnvioDesc(Usuario destinatario);
    
    // Contar mensajes recibidos por un usuario
    long countByDestinatario(Usuario destinatario);
    
    // Contar regalos por tipo para un usuario (para el ranking)
    @Query("SELECT COUNT(m) FROM Mensaje m WHERE m.destinatario = :usuario AND m.regaloTipo = :tipo")
    long countRegalosByDestinatarioAndTipo(@Param("usuario") Usuario usuario, @Param("tipo") String tipo);
    
    // Contar total de regalos recibidos por un usuario
    @Query("SELECT COUNT(m) FROM Mensaje m WHERE m.destinatario = :usuario")
    long countTotalRegalosByDestinatario(@Param("usuario") Usuario usuario);
}