package com.mycompany.myapp.service.mapper;


import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.service.dto.OrdarDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Ordar} and its DTO {@link OrdarDTO}.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, ProductMapper.class, StoreMapper.class, CustomerMapper.class})
public interface OrdarMapper extends EntityMapper<OrdarDTO, Ordar> {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.login", target = "userLogin")
    @Mapping(source = "store.id", target = "storeId")
    @Mapping(source = "customer.id", target = "customerId")
    OrdarDTO toDto(Ordar ordar);

    @Mapping(source = "userId", target = "user")
    @Mapping(target = "removeProduct", ignore = true)
    @Mapping(source = "storeId", target = "store")
    @Mapping(source = "customerId", target = "customer")
    Ordar toEntity(OrdarDTO ordarDTO);

    default Ordar fromId(Long id) {
        if (id == null) {
            return null;
        }
        Ordar ordar = new Ordar();
        ordar.setId(id);
        return ordar;
    }
}
