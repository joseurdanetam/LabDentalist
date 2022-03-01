package com.labdentalist.app.service.mapper;

import com.labdentalist.app.domain.Factura;
import com.labdentalist.app.service.dto.FacturaDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Factura} and its DTO {@link FacturaDTO}.
 */
@Mapper(componentModel = "spring", uses = { ClienteMapper.class, IntervencionMapper.class })
public interface FacturaMapper extends EntityMapper<FacturaDTO, Factura> {
    @Mapping(target = "cliente", source = "cliente", qualifiedByName = "id")
    FacturaDTO toDto(Factura s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    FacturaDTO toDtoId(Factura factura);
}
