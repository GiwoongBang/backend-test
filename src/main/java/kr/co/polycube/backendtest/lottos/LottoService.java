package kr.co.polycube.backendtest.lottos;

import kr.co.polycube.backendtest.lottos.dto.LottoResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class LottoService {

    private final LottoRepository lottoRepository;

    public LottoResponseDto issueLotto() {
        Set<Integer> numbeers = new HashSet<>();
        while (numbeers.size() < 6) {
            int num = (int) (Math.random() * 45) + 1;
            numbeers.add(num);
        }
        LottoEntity issuedLotto = new LottoEntity(numbeers);
        lottoRepository.save(issuedLotto);
        LottoResponseDto resData = LottoResponseDto.of(issuedLotto);

        return resData;
    }

}
