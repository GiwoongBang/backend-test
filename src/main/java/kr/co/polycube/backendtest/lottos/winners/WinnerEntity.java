package kr.co.polycube.backendtest.lottos.winners;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
@Entity
@Table(name = "winners")
public class WinnerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long lottoId;

    @Column(nullable = false)
    private int rank;

    public WinnerEntity(Long lottoId, int rank) {
        this.lottoId = lottoId;
        this.rank = rank;
    }
}