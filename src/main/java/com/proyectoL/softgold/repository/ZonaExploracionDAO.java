package com.proyectoL.softgold.repository;

import com.proyectoL.softgold.model.ZonaExploracion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ZonaExploracionDAO extends JpaRepository<ZonaExploracion, Long> {
    List<ZonaExploracion> findByEstado(String estado);
    List<ZonaExploracion> findByTipo(String tipo);
    List<ZonaExploracion> findByNombreContainingIgnoreCase(String nombre);
    List<ZonaExploracion> findByMina_CodMina(Long codMina);
}
