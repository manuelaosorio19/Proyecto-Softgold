package com.proyectoL.softgold.service;

import com.proyectoL.softgold.model.SupportTicket;
import com.proyectoL.softgold.repository.SupportTicketDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class SupportService implements SupportServiceIface {

    @Autowired
    private SupportTicketDAO supportTicketDAO;

    // opcional: notificar por correo al admin cuando se crea un ticket
    @Autowired(required = false)
    private JavaMailSender mailSender;

    @Override
    @Transactional
    public SupportTicket crearTicket(SupportTicket ticket) {
        SupportTicket saved = supportTicketDAO.save(ticket);

        // notificar (opcional)
        try {
            if (mailSender != null) {
                SimpleMailMessage msg = new SimpleMailMessage();
                msg.setTo("luisa.arcila28@gmail.com"); // ajustar admin
                msg.setSubject("[Soporte] " + ticket.getAsunto());
                msg.setText("Nuevo reporte de: " + ticket.getNombre() + " <" + ticket.getEmail() + ">\n\n"
                        + ticket.getDescripcion());
                mailSender.send(msg);
            }
        } catch (Exception ignored) {
            /* no romper flujo si falla el correo */ }

        return saved;
    }

    @Override
    @Transactional(readOnly = true)
    public List<SupportTicket> listarTickets() {
        return supportTicketDAO.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public SupportTicket buscarPorId(Long id) {
        Optional<SupportTicket> opt = supportTicketDAO.findById(id);
        return opt.orElse(null);
    }

    @Override
    @Transactional
    public void cerrarTicket(Long id) {
        SupportTicket t = buscarPorId(id);
        if (t != null) {
            t.setEstado("CLOSED");
            supportTicketDAO.save(t);
        }
    }
}