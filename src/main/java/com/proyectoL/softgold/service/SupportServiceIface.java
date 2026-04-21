package com.proyectoL.softgold.service;

import com.proyectoL.softgold.model.SupportTicket;
import java.util.List;

public interface SupportServiceIface {
    SupportTicket crearTicket(SupportTicket ticket);

    List<SupportTicket> listarTickets();

    SupportTicket buscarPorId(Long id);

    void cerrarTicket(Long id);
}