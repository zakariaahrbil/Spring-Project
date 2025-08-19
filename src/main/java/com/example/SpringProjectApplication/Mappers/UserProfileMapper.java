package com.example.SpringProjectApplication.Mappers;

import com.example.SpringProjectApplication.Dtos.UserProfileDto;
import com.example.SpringProjectApplication.Entities.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserProfileMapper
{
    UserProfileDto toDto(User user);

    User toEntity(UserProfileDto userProfileDto);

    List<UserProfileDto> toDto(List<User> users);
}
