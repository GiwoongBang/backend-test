package kr.co.polycube.backendtest.apis;

import kr.co.polycube.backendtest.lottos.WinnerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RequiredArgsConstructor
@RequestMapping("/winners")
@RestController
public class WinnerController {

    private final WinnerService winnerService;

    @GetMapping
    public Set<Integer> getWinningNumbers() {
        return winnerService.getWinningNumbers();
    }

    @PostMapping
    public void setWinningNumbers(@RequestBody String newWinningNumbers) {
        winnerService.setWinningNumbers(newWinningNumbers);
    }

}
