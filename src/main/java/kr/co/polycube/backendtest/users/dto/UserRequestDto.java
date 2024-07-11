package kr.co.polycube.backendtest.users.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserRequestDto {

    @NotBlank
    private String name;

    public UserRequestDto(String name) {
        this.name = name;
    }

}
