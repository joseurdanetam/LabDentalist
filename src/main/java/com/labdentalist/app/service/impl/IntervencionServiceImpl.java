package com.labdentalist.app.service.impl;

import com.labdentalist.app.domain.Intervencion;
import com.labdentalist.app.repository.IntervencionRepository;
import com.labdentalist.app.service.IntervencionService;
import com.labdentalist.app.service.dto.IntervencionDTO;
import com.labdentalist.app.service.mapper.IntervencionMapper;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Intervencion}.
 */
@Service
@Transactional
public class IntervencionServiceImpl implements IntervencionService {

    private final Logger log = LoggerFactory.getLogger(IntervencionServiceImpl.class);

    private final IntervencionRepository intervencionRepository;

    private final IntervencionMapper intervencionMapper;

    public IntervencionServiceImpl(IntervencionRepository intervencionRepository, IntervencionMapper intervencionMapper) {
        this.intervencionRepository = intervencionRepository;
        this.intervencionMapper = intervencionMapper;
    }

    @Override
    public IntervencionDTO save(IntervencionDTO intervencionDTO) {
        log.debug("Request to save Intervencion : {}", intervencionDTO);
        Intervencion intervencion = intervencionMapper.toEntity(intervencionDTO);
        intervencion = intervencionRepository.save(intervencion);
        return intervencionMapper.toDto(intervencion);
    }

    @Override
    public Optional<IntervencionDTO> partialUpdate(IntervencionDTO intervencionDTO) {
        log.debug("Request to partially update Intervencion : {}", intervencionDTO);

        return intervencionRepository
            .findById(intervencionDTO.getId())
            .map(existingIntervencion -> {
                intervencionMapper.partialUpdate(existingIntervencion, intervencionDTO);

                return existingIntervencion;
            })
            .map(intervencionRepository::save)
            .map(intervencionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<IntervencionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Intervencions");
        return intervencionRepository.findAll(pageable).map(intervencionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<IntervencionDTO> findOne(Long id) {
        log.debug("Request to get Intervencion : {}", id);
        return intervencionRepository.findById(id).map(intervencionMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Intervencion : {}", id);
        intervencionRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Intervencion> findAllByFacturaId(Long id) {
        log.debug("Request to delete Intervencion : {}", id);
        return intervencionRepository.findAllByFacturaId(id);
    }
}
