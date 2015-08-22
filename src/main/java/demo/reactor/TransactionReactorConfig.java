package demo.reactor;

import demo.jpa.entities.DepositTransaction;
import demo.jpa.entities.WithdrawalTransaction;
import demo.jpa.repositories.DepositRepository;
import demo.jpa.repositories.WithdrawalRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.bus.Event;
import reactor.bus.EventBus;

import javax.annotation.PostConstruct;

import static reactor.bus.selector.Selectors.$;

/**
 * Created by victor on 8/15/15.
 */
@Component
public class TransactionReactorConfig {
    private static final Logger LOG =
            LoggerFactory.getLogger(TransactionReactorConfig.class);
    @Autowired
    private EventBus eventBus;

    @Autowired
    private DepositRepository depositRepository;

    @Autowired
    private WithdrawalRepository withdrawalRepository;

    public WithdrawalRepository getWithdrawalRepository() {
        return this.withdrawalRepository;
    }

    public void setWithdrawalRepository(WithdrawalRepository withdrawalRepository) {
        this.withdrawalRepository = withdrawalRepository;
    }

    public DepositRepository getDepositRepository() {
        return this.depositRepository;
    }

    public void setDepositRepository(DepositRepository depositRepository) {
        this.depositRepository = depositRepository;
    }

    @PostConstruct
    public void init() {
        LOG.info("Initializing Reactor config.");
        eventBus.<Event<DepositTransaction>>on($("txn.deposit"), event -> {
            LOG.info("Reactor picking up deposit.");
            depositRepository.processDeposit(event.getData());
        });
        eventBus.<Event<WithdrawalTransaction>>on($("txn.withdrawal"), event -> {
            LOG.info("Reactor picking up withdrawal.");
            withdrawalRepository.processWithdrawal(event.getData());
        });
    }

    public EventBus getEventBus() {
        return eventBus;
    }

    public void setEventBus(EventBus eventBus) {
        this.eventBus = eventBus;
    }
}
