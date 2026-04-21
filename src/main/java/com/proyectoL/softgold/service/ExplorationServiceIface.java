package com.proyectoL.softgold.service;

import com.proyectoL.softgold.model.ZonaExploracion;
import java.util.List;
import java.util.Optional;

public interface ExplorationServiceIface {
    List<ZonaExploracion> listarTodas();
    Optional<ZonaExploracion> buscarPorId(Long id);
    ZonaExploracion guardar(ZonaExploracion zona);
    void eliminar(Long id);
    List<ZonaExploracion> filtrarPorEstado(String estado);
    List<ZonaExploracion> filtrarPorTipo(String tipo);
    List<ZonaExploracion> buscarPorNombre(String nombre);
}
