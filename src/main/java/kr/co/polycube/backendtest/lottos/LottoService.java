package kr.co.polycube.backendtest.lottos;

import kr.co.polycube.backendtest.lottos.dto.LottoResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.TreeSet;

@RequiredArgsConstructor
@Service
public class LottoService {

    private final LottoRepository lottoRepository;

    public LottoResponseDto issueLotto() {
        Set<Integer> setNumbers = new TreeSet<>();
        while (setNumbers.size() < 6) {
            int num = (int) (Math.random() * 45) + 1;
            setNumbers.add(num);
        }

        Integer[] numbers = setNumbers.toArray(new Integer[6]);
        LottoEntity issuedLotto = new LottoEntity(numbers);
        lottoRepository.save(issuedLotto);
        LottoResponseDto resData = LottoResponseDto.of(issuedLotto);

        return resData;
    }

}
