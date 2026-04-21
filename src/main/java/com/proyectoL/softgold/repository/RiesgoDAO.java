package com.proyectoL.softgold.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.proyectoL.softgold.model.Riesgo;

@Repository
public interface RiesgoDAO extends JpaRepository<Riesgo, Long> {

}
