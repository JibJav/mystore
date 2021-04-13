package com.mycompany.myapp.service.mapper;


import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.service.dto.CustomerDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Customer} and its DTO {@link CustomerDTO}.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, ProductMapper.class})
public interface CustomerMapper extends EntityMapper<CustomerDTO, Customer> {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.login", target = "userLogin")
    CustomerDTO toDto(Customer customer);

    @Mapping(source = "userId", target = "user")
    @Mapping(target = "ordars", ignore = true)
    @Mapping(target = "removeOrdar", ignore = true)
    @Mapping(target = "removeProduct", ignore = true)
    Customer toEntity(CustomerDTO customerDTO);

    default Customer fromId(Long id) {
        if (id == null) {
            return null;
        }
        Customer customer = new Customer();
        customer.setId(id);
        return customer;
    }
}
