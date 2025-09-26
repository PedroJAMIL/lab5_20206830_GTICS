package org.example.lab05_20206830.repository;

import jdk.jfr.Category;
import org.example.lab05_20206830.entity.Mensaje;
import org.example.lab05_20206830.entity.Ranking;
import org.example.lab05_20206830.entity.Usuario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer>{

    public List<Usuario> findByCategory(String nombre);
    @Query(value = "select * from Usuario", nativeQuery = true)
    List<Usuario> buscarTransPorCompName(String nombre);
}
