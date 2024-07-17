package kr.co.polycube.backendtest.lottos.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import kr.co.polycube.backendtest.lottos.LottoEntity;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LottoResponseDto {

    private Long id;

    private Integer number1;

    private Integer number2;

    private Integer number3;

    private Integer number4;

    private Integer number5;

    private Integer number6;

    public static LottoResponseDto of(LottoEntity issuedLotto) {

        return LottoResponseDto.builder()
                .id(issuedLotto.getId())
                .number1(issuedLotto.getNumber1())
                .number2(issuedLotto.getNumber2())
                .number3(issuedLotto.getNumber3())
                .number4(issuedLotto.getNumber4())
                .number5(issuedLotto.getNumber5())
                .number6(issuedLotto.getNumber6())
                .build();
    }

}
