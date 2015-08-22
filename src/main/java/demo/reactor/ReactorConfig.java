package demo.reactor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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

    //public EventBus eventBus;
    @Bean
    public EventBus eventBus() {
        ReactorConfig.LOG.info("Creating Event Bus.");
        return EventBus.create(this.getEnvironment());
    }

    public Environment getEnvironment() {
        ReactorConfig.LOG.info("Retornando env.");
        return environment;
    }

    public void setEnvironment(Environment environment) {
        ReactorConfig.LOG.info("Seteand env.");
        this.environment = environment;
    }

    @Bean
    public Environment getEnv() {
        ReactorConfig.LOG.info("Creando env.");
        return Environment.initializeIfEmpty();
    }
}
