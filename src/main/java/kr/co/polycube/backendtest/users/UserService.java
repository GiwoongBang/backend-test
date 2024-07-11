package kr.co.polycube.backendtest.users;

import kr.co.polycube.backendtest.users.dto.UserRequestDto;
import kr.co.polycube.backendtest.users.dto.UserResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    public UserResponseDto createUser(UserRequestDto userRequestDto) {

        UserEntity user = new UserEntity(userRequestDto);
        UserEntity savedUser = userRepository.save(user);
        UserResponseDto resData = UserResponseDto.id(savedUser);

        return resData;
    }

    public UserResponseDto getUser(Long id) {
        UserEntity foundUser = userCheckById(id);
        UserResponseDto resData = UserResponseDto.all(foundUser);

        return resData;
    }

    public UserResponseDto updateUser(Long id, String name) {
        UserEntity foundUser = userCheckById(id);
        foundUser.updateName(name);
        userRepository.save(foundUser);
        UserResponseDto resData = UserResponseDto.all(foundUser);

        return resData;
    }

    private UserEntity userCheckById(long id) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "유저를 찾을 수 없습니다."));

        return user;
    }
}