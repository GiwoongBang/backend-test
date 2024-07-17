package kr.co.polycube.backendtest.lottos.batch;

import kr.co.polycube.backendtest.lottos.*;
import kr.co.polycube.backendtest.lottos.winners.WinnerEntity;
import kr.co.polycube.backendtest.lottos.winners.WinnerRepository;
import kr.co.polycube.backendtest.lottos.winners.WinnerService;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.Set;

@Configuration
@EnableScheduling
public class LottoBatchConfiguration {

    @Autowired
    private LottoRepository lottoRepository;

    @Autowired
    private WinnerRepository winnerRepository;

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Autowired
    private WinnerService winnerService;

    @Bean
    public Job lottoJob() {
        return new JobBuilder("lottoJob", jobRepository)
                .start(lottoStep())
                .build();
    }

    @Bean
    public Step lottoStep() {
        return new StepBuilder("lottoStep", jobRepository)
                .<LottoEntity, WinnerEntity>chunk(10, transactionManager)
                .reader(lottoItemReader())
                .processor(lottoItemProcessor())
                .writer(winnerItemWriter())
                .build();
    }

    // 로또 번호 읽어오기
    @Bean
    public ItemReader<LottoEntity> lottoItemReader() {
        RepositoryItemReader<LottoEntity> reader = new RepositoryItemReader<>();
        reader.setRepository(lottoRepository);
        reader.setMethodName("findAll");
        reader.setSort(Collections.singletonMap("id", Sort.Direction.ASC));
        return reader;
    }

    // 로또 번호와 당첨 번호를 비교하고 당첨 및 등수 여부 확인
    @Bean
    public ItemProcessor<LottoEntity, WinnerEntity> lottoItemProcessor() {
        return lotto -> {
            // 당첨 번호 설정
            Set<Integer> winningNumbers = winnerService.getWinningNumbers();

            // 로또 번호와 당첨 번호를 비교하여 일치하는 번호의 개수 계산
            LottoEntity issuedLotto = lottoRepository.findById(lotto.getId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "유저를 찾을 수 없습니다."));

            int matchCount = 0;
            for (Integer number : issuedLotto.getNumbers()) {
                if (winningNumbers.contains(number)) {
                    matchCount++;
                }
            }

            // 등수 계산
            int rank = calculateRank(matchCount);
            if (rank > 0) {
                return new WinnerEntity(lotto.getId(), rank);
            } else {
                return null;
            }
        };
    }

    // 당첨자를 Winner 테이블에 저장
    @Bean
    public ItemWriter<WinnerEntity> winnerItemWriter() {
        return items -> {
            for (WinnerEntity winner : items) {
                if (winner != null) {
                    winnerRepository.save(winner);
                }
            }
        };
    }

    private int calculateRank(int matchCount) {
        return switch (matchCount) {
            case 6 -> 1;
            case 5 -> 2;
            case 4 -> 3;
            case 3 -> 4;
            default -> 0;
        };
    }

    @Scheduled(cron = "0 0 0 * * SUN")
    public void runLottoBatch() {
        try {
            JobParameters jobParameters = new JobParametersBuilder()
                    .addLong("time", System.currentTimeMillis())
                    .toJobParameters();
            jobLauncher.run(lottoJob(), jobParameters);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}