package kr.co.polycube.backendtest.lottos.winners;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WinnerRepository extends JpaRepository<WinnerEntity, Long> {



}
