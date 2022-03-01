package com.labdentalist.app.service.mapper;

import com.labdentalist.app.domain.Receta;
import com.labdentalist.app.service.dto.RecetaDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Receta} and its DTO {@link RecetaDTO}.
 */
@Mapper(componentModel = "spring", uses = { ClienteMapper.class })
public interface RecetaMapper extends EntityMapper<RecetaDTO, Receta> {
    @Mapping(target = "cliente", source = "cliente", qualifiedByName = "id")
    RecetaDTO toDto(Receta s);
}
