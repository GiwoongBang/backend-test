package kr.co.polycube.backendtest.apis;

import kr.co.polycube.backendtest.lottos.winners.WinnerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.TreeSet;

@RequiredArgsConstructor
@RequestMapping("/winners")
@RestController
public class WinnerController {

    private final WinnerService winnerService;

    @GetMapping
    public ResponseEntity<TreeSet<Integer>> getWinningNumbers() {
        TreeSet<Integer> resData = winnerService.getWinningNumbers();

        return new ResponseEntity<>(resData, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<TreeSet<Integer>> setWinningNumbers(@RequestBody String newWinningNumbers) {
        TreeSet<Integer> resData = winnerService.setWinningNumbers(newWinningNumbers);

        return new ResponseEntity<>(resData, HttpStatus.OK);
    }

}
