package kr.co.polycube.backendtest.apis;

import jakarta.validation.Valid;
import kr.co.polycube.backendtest.users.UserService;
import kr.co.polycube.backendtest.users.dto.UserRequestDto;
import kr.co.polycube.backendtest.users.dto.UserResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/users")
@RestController
public class UserController {

    private final UserService userService;

    @PostMapping()
    public ResponseEntity<UserResponseDto> createUser(@Valid @RequestBody UserRequestDto userRequestDto) {
        UserResponseDto resData = userService.createUser(userRequestDto);

        return new ResponseEntity<>(resData, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUser(@PathVariable("id") Long id) {
        UserResponseDto resData = userService.getUser(id);

        return new ResponseEntity<>(resData, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable("id") Long id,
                                                      @RequestParam("name") String name) {
        UserResponseDto resData = userService.updateUser(id, name);

        return new ResponseEntity<>(resData, HttpStatus.OK);
    }

}
