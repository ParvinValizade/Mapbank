package com.company.map.dto.converter;

import com.company.map.dto.UserDto;
import com.company.map.model.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserDtoConverter {

    public UserDto convertToUserDto(User user){
        return new UserDto(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                user.getRole()
        );
    }

    public List<UserDto> convertToUserDtoList(List<User> userList){
        return userList.stream()
                .map(this::convertToUserDto).collect(Collectors.toList());
    }
}
