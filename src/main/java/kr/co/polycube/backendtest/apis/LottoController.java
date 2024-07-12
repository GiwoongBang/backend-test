package kr.co.polycube.backendtest.apis;

import kr.co.polycube.backendtest.lottos.LottoService;
import kr.co.polycube.backendtest.lottos.dto.LottoResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/lottos")
@RestController
public class LottoController {

    private final LottoService lottoService;

    @PostMapping()
    public ResponseEntity<LottoResponseDto> issueLotto() {
        LottoResponseDto resData = lottoService.issueLotto();

        return new ResponseEntity<>(resData, HttpStatus.OK);
    }

}
