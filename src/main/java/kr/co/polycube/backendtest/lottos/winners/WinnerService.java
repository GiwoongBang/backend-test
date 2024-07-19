package kr.co.polycube.backendtest.lottos.winners;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeSet;

@RequiredArgsConstructor
@Service
public class WinnerService {

    private static TreeSet<Integer> winningNumbers = new TreeSet<>(Set.of(46, 47, 48, 49, 50, 51));

    public TreeSet<Integer> getWinningNumbers() {
        if(winningNumbers.size() != 6) {
            throw new IllegalStateException("당첨 번호의 개수는 6개입니다. 당첨 번호를 다시 설정해주세요.");
        }

        TreeSet<Integer> resData = winningNumbers;

        return winningNumbers;
    }

    public TreeSet<Integer> setWinningNumbers(String newWinningNumbers) {
        StringTokenizer tokenizer = new StringTokenizer(newWinningNumbers, " ");
        TreeSet<Integer> numbers = new TreeSet<>();

        while (tokenizer.hasMoreTokens()) {
            numbers.add(Integer.parseInt(tokenizer.nextToken()));
        }

        if (numbers.size() != 6) {
            throw new IllegalArgumentException("6개의 고유한 숫자를 입력해야 합니다.");
        }

        winningNumbers = numbers;
        TreeSet<Integer> resData = winningNumbers;

        return resData;
    }

}
