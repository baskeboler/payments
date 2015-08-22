package demo.reactor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import reactor.Environment;
import reactor.bus.EventBus;
import reactor.spring.context.config.EnableReactor;

/**
 * Created by victor on 8/15/15.
 */
@Configuration
@EnableReactor
public class ReactorConfig {

    private static final Logger LOG = LoggerFactory.getLogger(ReactorConfig.class);
    @Autowired
    private Environment environment;

    public Environment getEnvironment() {
        LOG.info("Retornando env.");
        return this.environment;
    }

    public void setEnvironment(Environment environment) {
        LOG.info("Seteand env.");
        this.environment = environment;
    }

    //public EventBus eventBus;
    @Bean
    public EventBus eventBus() {
        LOG.info("Creating Event Bus.");
        EventBus bus = EventBus.create(getEnvironment());
        return bus;
    }

    @Bean
    public Environment getEnv() {
        LOG.info("Creando env.");
        return Environment.initializeIfEmpty();
    }
}
