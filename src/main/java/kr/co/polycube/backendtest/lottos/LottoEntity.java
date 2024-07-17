package kr.co.polycube.backendtest.lottos;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "lottos")
public class LottoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Integer number1;

    @Column
    private Integer number2;

    @Column
    private Integer number3;

    @Column
    private Integer number4;

    @Column
    private Integer number5;

    @Column
    private Integer number6;

    public LottoEntity(Integer[] numbers) {
        this.number1 = numbers[0];
        this.number2 = numbers[1];
        this.number3 = numbers[2];
        this.number4 = numbers[3];
        this.number5 = numbers[4];
        this.number6 = numbers[5];
    }

    public List<Integer> getNumbers() {
        return Arrays.asList(number1, number2, number3, number4, number5, number6);
    }

}
