package kr.co.polycube.backendtest.lottos;

import kr.co.polycube.backendtest.BackendTestApplication;
import kr.co.polycube.backendtest.lottos.batch.LottoBatchConfiguration;
import kr.co.polycube.backendtest.lottos.winners.WinnerEntity;
import kr.co.polycube.backendtest.lottos.winners.WinnerRepository;
import kr.co.polycube.backendtest.lottos.winners.WinnerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBatchTest
@SpringJUnitConfig(classes = LottoBatchConfiguration.class)
@SpringBootTest(classes = BackendTestApplication.class)
public class LottoBatchConfigurationTest {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job lottoJob;

    @Autowired
    private LottoRepository lottoRepository;

    @Autowired
    private WinnerRepository winnerRepository;

    @Autowired
    private WinnerService winnerService;

    @BeforeEach
    void setUp() throws Exception {
        lottoRepository.deleteAll();
        winnerRepository.deleteAll();

        Integer[] lottoNumbers1 = {1, 2, 3, 4, 5, 6};
        Integer[] lottoNumbers2 = {3, 4, 5, 6, 7, 8};
        Integer[] lottoNumbers3 = {7, 8, 9, 10, 11, 12};
        lottoRepository.save(new LottoEntity(lottoNumbers1));
        lottoRepository.save(new LottoEntity(lottoNumbers2));
        lottoRepository.save(new LottoEntity(lottoNumbers3));

        String winningNumbers = "1 2 3 4 5 6";
        winnerService.setWinningNumbers(winningNumbers);
    }

    @Test
    void 로또_당첨_여부_배치_처리() throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        JobExecution jobExecution = jobLauncher.run(
                lottoJob,
                new JobParametersBuilder()
                        .addLong("time", System.currentTimeMillis())
                        .toJobParameters()
        );

        assertThat(jobExecution.getExitStatus().getExitCode()).isEqualTo("COMPLETED");
        assertThat(winnerRepository.findAll()).hasSize(2);

        WinnerEntity winner = winnerRepository.findAll().get(0);
        assertThat(winner.getRank()).isEqualTo(1);
    }
}
