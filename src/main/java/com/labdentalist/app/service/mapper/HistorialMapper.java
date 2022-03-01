package com.labdentalist.app.service.mapper;

import com.labdentalist.app.domain.Historial;
import com.labdentalist.app.service.dto.HistorialDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Historial} and its DTO {@link HistorialDTO}.
 */
@Mapper(componentModel = "spring", uses = { ClienteMapper.class })
public interface HistorialMapper extends EntityMapper<HistorialDTO, Historial> {
    @Mapping(target = "cliente", source = "cliente", qualifiedByName = "id")
    HistorialDTO toDto(Historial s);
}
