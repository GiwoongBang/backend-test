package kr.co.polycube.backendtest.users;

import jakarta.persistence.*;
import kr.co.polycube.backendtest.users.dto.UserRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    public UserEntity(UserRequestDto userRequestDto) {
        this.name = userRequestDto.getName();
    }

    public void updateName(String name) {
        this.name = name;
    }

}


