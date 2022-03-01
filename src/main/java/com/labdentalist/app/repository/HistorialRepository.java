package com.labdentalist.app.repository;

import com.labdentalist.app.domain.Cliente;
import com.labdentalist.app.domain.Factura;
import com.labdentalist.app.domain.Historial;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Historial entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HistorialRepository extends JpaRepository<Historial, Long> {}
