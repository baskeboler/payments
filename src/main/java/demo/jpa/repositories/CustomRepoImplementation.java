package demo.jpa.repositories;

import demo.jpa.entities.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.GenericTypeResolver;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaEntityInformationSupport;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Created by victor on 8/17/15.
 */
public abstract class CustomRepoImplementation<T extends Transaction> {

    private static final Logger LOG = LoggerFactory.getLogger(CustomRepoImplementation.class);
    @PersistenceContext
    private EntityManager em;

    private Class<T> genericType;

    private JpaEntityInformation<T, ?> entityInformation;

    @PostConstruct
    public void postConstruct() {
        LOG.info("Running post-construct.");
        genericType = (Class<T>) GenericTypeResolver.resolveTypeArgument(getClass(), CustomRepoImplementation.class);
        entityInformation = JpaEntityInformationSupport.getMetadata(genericType, em);
        LOG.info("Building repository for {}", genericType.getSimpleName());
    }

    public EntityManager getEm() {
        return this.em;
    }

    public void setEm(EntityManager em) {
        this.em = em;
    }

    public JpaEntityInformation<T, ?> getEntityInformation() {
        return this.entityInformation;
    }

    public void setEntityInformation(JpaEntityInformation<T, ?> entityInformation) {
        this.entityInformation = entityInformation;
    }

    protected T save(T txn) {
        if (entityInformation.isNew(txn)) {
            em.persist(txn);
            return txn;
        } else {
            return em.merge(txn);
        }
    }
}
