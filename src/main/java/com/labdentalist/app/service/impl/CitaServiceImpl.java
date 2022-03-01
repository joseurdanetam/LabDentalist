package com.labdentalist.app.service.impl;

import com.labdentalist.app.domain.Cita;
import com.labdentalist.app.domain.Cliente;
import com.labdentalist.app.repository.CitaRepository;
import com.labdentalist.app.repository.specification.CitaSpecification;
import com.labdentalist.app.service.CitaService;
import com.labdentalist.app.service.dto.CitaDTO;
import com.labdentalist.app.service.mapper.CitaMapper;
import java.time.Instant;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Cita}.
 */
@Service
@Transactional
public class CitaServiceImpl implements CitaService {

    private final Logger log = LoggerFactory.getLogger(CitaServiceImpl.class);

    private final CitaRepository citaRepository;

    private final CitaMapper citaMapper;

    public CitaServiceImpl(CitaRepository citaRepository, CitaMapper citaMapper) {
        this.citaRepository = citaRepository;
        this.citaMapper = citaMapper;
    }

    @Override
    public CitaDTO save(CitaDTO citaDTO) {
        log.debug("Request to save Cita : {}", citaDTO);
        Cita cita = citaMapper.toEntity(citaDTO);
        cita = citaRepository.save(cita);
        return citaMapper.toDto(cita);
    }

    @Override
    public Optional<CitaDTO> partialUpdate(CitaDTO citaDTO) {
        log.debug("Request to partially update Cita : {}", citaDTO);

        return citaRepository
            .findById(citaDTO.getId())
            .map(existingCita -> {
                citaMapper.partialUpdate(existingCita, citaDTO);

                return existingCita;
            })
            .map(citaRepository::save)
            .map(citaMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CitaDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Citas");
        return citaRepository.findAll(pageable).map(citaMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CitaDTO> findOne(Long id) {
        log.debug("Request to get Cita : {}", id);
        return citaRepository.findById(id).map(citaMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Cita : {}", id);
        citaRepository.deleteById(id);
    }

    @Override
    public Page<Cita> findAllByFechaCitaGreaterThanEqual(Instant now, Pageable pageable) {
        return this.citaRepository.findAllByFechaCitaGreaterThanEqual(now, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CitaDTO> buscarCitas(String filtro, Pageable pageable) {
        return citaRepository.findAll(CitaSpecification.busquedaCitas(filtro), pageable).map(citaMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Cita> findAllByClienteId(Long id, Pageable pageable) {
        return citaRepository.findAllByClienteId(id, pageable);
    }
}
