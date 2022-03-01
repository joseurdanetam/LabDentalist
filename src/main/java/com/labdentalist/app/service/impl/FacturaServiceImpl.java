package com.labdentalist.app.service.impl;

import com.labdentalist.app.domain.Factura;
import com.labdentalist.app.repository.FacturaRepository;
import com.labdentalist.app.repository.specification.FacturaSpecification;
import com.labdentalist.app.service.FacturaService;
import com.labdentalist.app.service.dto.FacturaDTO;
import com.labdentalist.app.service.mapper.FacturaMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Factura}.
 */
@Service
@Transactional
public class FacturaServiceImpl implements FacturaService {

    private final Logger log = LoggerFactory.getLogger(FacturaServiceImpl.class);

    private final FacturaRepository facturaRepository;

    private final FacturaMapper facturaMapper;

    public FacturaServiceImpl(FacturaRepository facturaRepository, FacturaMapper facturaMapper) {
        this.facturaRepository = facturaRepository;
        this.facturaMapper = facturaMapper;
    }

    @Override
    public FacturaDTO save(FacturaDTO facturaDTO) {
        log.debug("Request to save Factura : {}", facturaDTO);
        Factura factura = facturaMapper.toEntity(facturaDTO);
        factura = facturaRepository.save(factura);
        return facturaMapper.toDto(factura);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Factura> findAllByClienteId(Long id, Pageable pageable) {
        log.debug("Request to get all Historials");
        return facturaRepository.findAllByClienteId(id, pageable);
    }

    @Override
    public Optional<FacturaDTO> partialUpdate(FacturaDTO facturaDTO) {
        log.debug("Request to partially update Factura : {}", facturaDTO);

        return facturaRepository
            .findById(facturaDTO.getId())
            .map(existingFactura -> {
                facturaMapper.partialUpdate(existingFactura, facturaDTO);

                return existingFactura;
            })
            .map(facturaRepository::save)
            .map(facturaMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FacturaDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Facturas");
        return facturaRepository.findAll(pageable).map(facturaMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FacturaDTO> findOne(Long id) {
        log.debug("Request to get Factura : {}", id);
        return facturaRepository.findById(id).map(facturaMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Factura : {}", id);
        facturaRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FacturaDTO> busquedaFacturas(String filtro, Pageable pageable) {
        return facturaRepository.findAll(FacturaSpecification.busquedaFacturas(filtro), pageable).map(facturaMapper::toDto);
    }
}
