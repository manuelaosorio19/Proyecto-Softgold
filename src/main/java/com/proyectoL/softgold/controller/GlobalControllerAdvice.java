package com.proyectoL.softgold.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalControllerAdvice {

    @ModelAttribute("requestURI")
    public String requestURI(HttpServletRequest request) {
        return request.getRequestURI();
    }

    @ModelAttribute("isAdmin")
    public boolean isAdmin(Authentication auth) {
        if (auth == null || !auth.isAuthenticated()) return false;
        return auth.getAuthorities().stream()
            .anyMatch(a -> a.getAuthority().equals("ROLE_ADMINISTRADOR"));
    }

    @ModelAttribute("isAuthenticated")
    public boolean isAuthenticated(Authentication auth) {
        return auth != null && auth.isAuthenticated()
            && !"anonymousUser".equals(auth.getPrincipal().toString());
    }

    @ModelAttribute("currentUsername")
    public String currentUsername(Authentication auth) {
        if (auth == null || !auth.isAuthenticated()) return null;
        if ("anonymousUser".equals(auth.getPrincipal().toString())) return null;
        return auth.getName();
    }
}
