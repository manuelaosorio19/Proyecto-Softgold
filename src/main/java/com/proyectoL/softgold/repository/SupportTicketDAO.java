package com.proyectoL.softgold.repository;

import com.proyectoL.softgold.model.SupportTicket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SupportTicketDAO extends JpaRepository<SupportTicket, Long> {
    // puedes agregar consultas personalizadas si las necesitas
}