package kr.co.polycube.backendtest.lottos.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import kr.co.polycube.backendtest.lottos.LottoEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;

@Getter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LottoResponseDto {

    private Set<Integer> numbers;

    @Builder
    public LottoResponseDto(Set<Integer> numbers) {
        this.numbers = numbers;
    }

    public static LottoResponseDto of(LottoEntity issuedLotto) {

        return LottoResponseDto.builder()
                .numbers(issuedLotto.getNumbers())
                .build();
    }

}
