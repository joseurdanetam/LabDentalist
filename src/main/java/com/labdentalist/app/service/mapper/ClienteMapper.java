package com.labdentalist.app.service.mapper;

import com.labdentalist.app.domain.Cliente;
import com.labdentalist.app.service.dto.ClienteDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Cliente} and its DTO {@link ClienteDTO}.
 */
@Mapper(componentModel = "spring", uses = { CitaMapper.class })
public interface ClienteMapper extends EntityMapper<ClienteDTO, Cliente> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "dni", source = "dni")
    @Mapping(target = "nombre", source = "nombre")
    @Mapping(target = "apellido", source = "apellido")
    @Mapping(target = "sexo", source = "sexo")
    @Mapping(target = "edad", source = "edad")
    @Mapping(target = "observacion", source = "observacion")
    ClienteDTO toDtoId(Cliente cliente);
}
