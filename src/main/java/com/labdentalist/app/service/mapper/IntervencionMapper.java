package com.labdentalist.app.service.mapper;

import com.labdentalist.app.domain.Intervencion;
import com.labdentalist.app.service.dto.IntervencionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Intervencion} and its DTO {@link IntervencionDTO}.
 */
@Mapper(componentModel = "spring", uses = { CitaMapper.class, FacturaMapper.class, ClienteMapper.class })
public interface IntervencionMapper extends EntityMapper<IntervencionDTO, Intervencion> {
    @Mapping(target = "cita", source = "cita", qualifiedByName = "id")
    @Mapping(target = "factura", source = "factura", qualifiedByName = "id")
    @Mapping(target = "cliente", source = "cliente", qualifiedByName = "id")
    @Mapping(target = "titulo", source = "titulo")
    @Mapping(target = "precioUnitario", source = "precioUnitario")
    IntervencionDTO toDto(Intervencion s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    IntervencionDTO toDtoId(Intervencion intervencion);
}
