package kr.co.polycube.backendtest.lottos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LottoRepository extends JpaRepository<LottoEntity, Long> {

}