package demo.services.batch;

import demo.jpa.entities.DepositTransaction;
import demo.jpa.repositories.DepositRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.Collections;

/**
 * Created by victor on 8/12/15.
 */
//@Configuration
//@EnableBatchProcessing
public class TxnBatch {
    private static final Logger LOG = LoggerFactory.getLogger(TxnBatch.class);
    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    public JobBuilderFactory getJobBuilderFactory() {
        return this.jobBuilderFactory;
    }

    public void setJobBuilderFactory(JobBuilderFactory jobBuilderFactory) {
        this.jobBuilderFactory = jobBuilderFactory;
    }

    public StepBuilderFactory getStepBuilderFactory() {
        return this.stepBuilderFactory;
    }

    public void setStepBuilderFactory(StepBuilderFactory stepBuilderFactory) {
        this.stepBuilderFactory = stepBuilderFactory;
    }

    public DepositRepository getDepositRepository() {
        return this.depositRepository;
    }

    public void setDepositRepository(DepositRepository depositRepository) {
        this.depositRepository = depositRepository;
    }

    @Autowired

    private DepositRepository depositRepository;

    @Bean
    public ItemReader<DepositTransaction> reader() {
        LOG.info("Getting reader");
        RepositoryItemReader<DepositTransaction> ret = new RepositoryItemReader<>();
        ret.setRepository(depositRepository);
        ret.setMethodName("findByProcessed");
        ret.setSort(Collections.singletonMap("id", Sort.Direction.ASC));
        ret.setArguments(Arrays.asList(Boolean.FALSE));
        return ret;
    }

    @Bean
    public ItemProcessor<DepositTransaction, DepositTransaction> processor() {
        LOG.info("Getting processor");
        return new DepositProcessor();
    }

    @Bean
    public ItemWriter<DepositTransaction> writer() {
        RepositoryItemWriter<DepositTransaction> w = new RepositoryItemWriter<>();
        w.setRepository(depositRepository);
        w.setMethodName("processDeposit");
        return w;
    }

    @Bean
    public Job job(Step step, JobExecutionListener listener) {
        return jobBuilderFactory.get("depositProcessor")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(step)
                .end()
                .build();
    }

    @Bean
    public Step step(ItemReader<DepositTransaction> reader,
                     ItemProcessor<DepositTransaction,
                             DepositTransaction> processor,
                     ItemWriter<DepositTransaction> writer) {
        return stepBuilderFactory.get("depositStep")
                .<DepositTransaction, DepositTransaction>chunk(3)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }
}
