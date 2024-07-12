package kr.co.polycube.backendtest.lottos;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "lottos")
public class LottoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Convert(converter = SetToStringConverter.class)
    @Column(name = "numbers")
    private Set<Integer> numbers;

    public LottoEntity(Set<Integer> numbers) {
        this.numbers = numbers;
    }

}
