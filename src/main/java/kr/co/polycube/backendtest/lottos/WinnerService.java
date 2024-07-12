package kr.co.polycube.backendtest.lottos;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

@RequiredArgsConstructor
@Service
public class WinnerService {

    private Set<Integer> winningNumbers = new HashSet<>(Set.of(46, 47, 48, 49, 50, 51));

    public Set<Integer> getWinningNumbers() {
        return winningNumbers;
    }

    public void setWinningNumbers(String newWinningNumbers) {
        StringTokenizer tokenizer = new StringTokenizer(newWinningNumbers, " ");
        Set<Integer> numbers = new HashSet<>();

        while (tokenizer.hasMoreTokens()) {
            numbers.add(Integer.parseInt(tokenizer.nextToken()));
        }

        if (numbers.size() != 6) {
            throw new IllegalArgumentException("6개의 고유한 숫자를 입력해야 합니다.");
        }

        this.winningNumbers = numbers;
    }

}
