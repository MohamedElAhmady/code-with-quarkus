package org.stibodx.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.Mapping;
import org.stibodx.dto.AddressDTO;
import org.stibodx.entity.Address;

@Mapper(componentModel = MappingConstants.ComponentModel.CDI)
public interface AddressMapper {
    AddressDTO toDTO(Address address);
    
    @Mapping(target = "user", ignore = true)
    Address toEntity(AddressDTO addressDTO);
}
