package com.evanw.datebyrate.mappers;

import com.evanw.datebyrate.Dto.user.UserResponseDto;
import com.evanw.datebyrate.models.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserResponseDto toUserResponseDtoFromUser(User user) {
        return new UserResponseDto(
                user.getId(),
                user.getFirstName(),
                user.getAge(),
                user.getGender(),
                user.getSexualOrientation()
        );
    }
}
