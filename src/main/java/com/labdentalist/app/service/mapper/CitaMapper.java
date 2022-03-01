package com.labdentalist.app.service.mapper;

import com.labdentalist.app.domain.Cita;
import com.labdentalist.app.service.dto.CitaDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Cita} and its DTO {@link CitaDTO}.
 */
@Mapper(componentModel = "spring", uses = { ClienteMapper.class, UserMapper.class })
public interface CitaMapper extends EntityMapper<CitaDTO, Cita> {
    @Mapping(target = "cliente", source = "cliente", qualifiedByName = "id")
    @Mapping(target = "user", source = "user", qualifiedByName = "id")
    CitaDTO toDto(Cita s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CitaDTO toDtoId(Cita cita);
}
