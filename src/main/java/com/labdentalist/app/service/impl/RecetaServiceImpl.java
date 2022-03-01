package com.labdentalist.app.service.impl;

import com.labdentalist.app.domain.Receta;
import com.labdentalist.app.repository.RecetaRepository;
import com.labdentalist.app.repository.specification.RecetaSpecification;
import com.labdentalist.app.service.RecetaService;
import com.labdentalist.app.service.dto.RecetaDTO;
import com.labdentalist.app.service.mapper.RecetaMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Receta}.
 */
@Service
@Transactional
public class RecetaServiceImpl implements RecetaService {

    private final Logger log = LoggerFactory.getLogger(RecetaServiceImpl.class);

    private final RecetaRepository recetaRepository;

    private final RecetaMapper recetaMapper;

    public RecetaServiceImpl(RecetaRepository recetaRepository, RecetaMapper recetaMapper) {
        this.recetaRepository = recetaRepository;
        this.recetaMapper = recetaMapper;
    }

    @Override
    public RecetaDTO save(RecetaDTO recetaDTO) {
        log.debug("Request to save Receta : {}", recetaDTO);
        Receta receta = recetaMapper.toEntity(recetaDTO);
        receta = recetaRepository.save(receta);
        return recetaMapper.toDto(receta);
    }

    @Override
    public Optional<RecetaDTO> partialUpdate(RecetaDTO recetaDTO) {
        log.debug("Request to partially update Receta : {}", recetaDTO);

        return recetaRepository
            .findById(recetaDTO.getId())
            .map(existingReceta -> {
                recetaMapper.partialUpdate(existingReceta, recetaDTO);

                return existingReceta;
            })
            .map(recetaRepository::save)
            .map(recetaMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Receta> searchRecetaById(Long id, Pageable pageable) {
        log.debug("Request to get all recetas by Cliente");
        return recetaRepository.findAllByClienteId(id, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RecetaDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Recetas");
        return recetaRepository.findAll(pageable).map(recetaMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RecetaDTO> findOne(Long id) {
        log.debug("Request to get Receta : {}", id);
        return recetaRepository.findById(id).map(recetaMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Receta : {}", id);
        recetaRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RecetaDTO> busquedaRecetas(String filtro, Pageable pageable) {
        return recetaRepository.findAll(RecetaSpecification.busquedaRecetas(filtro), pageable).map(recetaMapper::toDto);
    }
}
