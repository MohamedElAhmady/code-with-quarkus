package org.stibodx.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.AfterMapping;
import org.mapstruct.MappingTarget;
import org.stibodx.dto.UserDTO;
import org.stibodx.entity.User;

@Mapper(componentModel = MappingConstants.ComponentModel.CDI, uses = AddressMapper.class)
public interface UserMapper {

    User toEntity(UserDTO userDTO);
    
    UserDTO toDTO(User user);
    
    @AfterMapping
    default void setUserInAddress(@MappingTarget User user) {
        if (user.getAddress() != null) {
            user.getAddress().setUser(user);
        }
    }
}