package kr.co.polycube.backendtest.users.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import kr.co.polycube.backendtest.users.UserEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponseDto {

    private Long id;

    private String name;

    @Builder
    private UserResponseDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static UserResponseDto id(UserEntity user) {

        return UserResponseDto.builder()
                .id(user.getId())
                .build();
    }

    public static UserResponseDto all(UserEntity user) {

        return UserResponseDto.builder()
                .id(user.getId())
                .name(user.getName())
                .build();
    }

}
