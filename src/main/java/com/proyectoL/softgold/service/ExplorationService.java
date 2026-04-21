package com.proyectoL.softgold.service;

import com.proyectoL.softgold.model.ZonaExploracion;
import com.proyectoL.softgold.repository.ZonaExploracionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ExplorationService implements ExplorationServiceIface {

    @Autowired
    private ZonaExploracionDAO zonaDAO;

    @Override
    public List<ZonaExploracion> listarTodas() {
        return zonaDAO.findAll();
    }

    @Override
    public Optional<ZonaExploracion> buscarPorId(Long id) {
        return zonaDAO.findById(id);
    }

    @Override
    public ZonaExploracion guardar(ZonaExploracion zona) {
        return zonaDAO.save(zona);
    }

    @Override
    public void eliminar(Long id) {
        zonaDAO.deleteById(id);
    }

    @Override
    public List<ZonaExploracion> filtrarPorEstado(String estado) {
        return zonaDAO.findByEstado(estado);
    }

    @Override
    public List<ZonaExploracion> filtrarPorTipo(String tipo) {
        return zonaDAO.findByTipo(tipo);
    }

    @Override
    public List<ZonaExploracion> buscarPorNombre(String nombre) {
        return zonaDAO.findByNombreContainingIgnoreCase(nombre);
    }
}
