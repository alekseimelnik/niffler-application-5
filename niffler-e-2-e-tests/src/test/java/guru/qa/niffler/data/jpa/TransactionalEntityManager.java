package guru.qa.niffler.data.jpa;

import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaDelete;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.CriteriaUpdate;
import jakarta.persistence.metamodel.Metamodel;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

public class TransactionalEntityManager implements EntityManager {

    private final EntityManager delegate;

    public TransactionalEntityManager(EntityManager delegate) {
        this.delegate = delegate;
    }

    private void tx(Consumer<EntityManager> consumer){
        EntityTransaction entityTransaction = delegate.getTransaction();
        entityTransaction.begin();
        try {
            consumer.accept(delegate);
            entityTransaction.commit();
        } catch (Exception e) {
            entityTransaction.rollback();
            throw e;
        }
    }

    private <T> T txWithResult(Function<EntityManager, T> consumer){
        EntityTransaction entityTransaction = delegate.getTransaction();
        entityTransaction.begin();
        try {
            T result = consumer.apply(delegate);
            entityTransaction.commit();
            return result;
        } catch (Exception e) {
            entityTransaction.rollback();
            throw e;
        }
    }

    @Override
    public void persist(Object entity) {
        tx(em -> em.persist(entity));
    }

    @Override
    public <T> T merge(T entity) {
        return txWithResult(em -> em.merge(entity));
    }

    @Override
    public void remove(Object entity) {
        tx(em -> em.remove(entity));
    }

    @Override
    public <T> T find(Class<T> aClass, Object entity) {
        return delegate.find(aClass, entity);
    }

    @Override
    public <T> T find(Class<T> aClass, Object entity, Map<String, Object> map) {
        return delegate.find(aClass, entity, map);
    }

    @Override
    public <T> T find(Class<T> aClass, Object entity, LockModeType lockModeType) {
        return delegate.find(aClass, entity, lockModeType);
    }

    @Override
    public <T> T find(Class<T> aClass, Object entity, LockModeType lockModeType, Map<String, Object> map) {
        return delegate.find(aClass, entity, lockModeType, map);
    }

    @Override
    public <T> T getReference(Class<T> aClass, Object entity) {
        return delegate.getReference(aClass, entity);
    }

    @Override
    public void flush() {
        delegate.flush();
    }

    @Override
    public void setFlushMode(FlushModeType flushModeType) {
        delegate.setFlushMode(flushModeType);
    }

    @Override
    public FlushModeType getFlushMode() {
        return delegate.getFlushMode();
    }

    @Override
    public void lock(Object entity, LockModeType lockModeType) {
        delegate.lock(entity, lockModeType);
    }

    @Override
    public void lock(Object entity, LockModeType lockModeType, Map<String, Object> map) {
        delegate.lock(entity, lockModeType, map);
    }

    @Override
    public void refresh(Object entity) {
        delegate.refresh(entity);
    }

    @Override
    public void refresh(Object entity, Map<String, Object> map) {
        delegate.refresh(entity, map);
    }

    @Override
    public void refresh(Object entity, LockModeType lockModeType) {
        delegate.refresh(entity, lockModeType);
    }

    @Override
    public void refresh(Object entity, LockModeType lockModeType, Map<String, Object> map) {
        delegate.refresh(entity, lockModeType, map);
    }

    @Override
    public void clear() {
        delegate.clear();
    }

    @Override
    public void detach(Object entity) {
        delegate.detach(entity);
    }

    @Override
    public boolean contains(Object entity) {
        return delegate.contains(entity);
    }

    @Override
    public LockModeType getLockMode(Object entity) {
        return delegate.getLockMode(entity);
    }

    @Override
    public void setProperty(String s, Object entity) {
        delegate.setProperty(s, entity);
    }

    @Override
    public Map<String, Object> getProperties() {
        return delegate.getProperties();
    }

    @Override
    public Query createQuery(String s) {
        return delegate.createQuery(s);
    }

    @Override
    public <T> TypedQuery<T> createQuery(CriteriaQuery<T> criteriaQuery) {
        return delegate.createQuery(criteriaQuery);
    }

    @Override
    public Query createQuery(CriteriaUpdate criteriaUpdate) {
        return delegate.createQuery(criteriaUpdate);
    }

    @Override
    public Query createQuery(CriteriaDelete criteriaDelete) {
        return delegate.createQuery(criteriaDelete);
    }

    @Override
    public <T> TypedQuery<T> createQuery(String s, Class<T> aClass) {
        return delegate.createQuery(s, aClass);
    }

    @Override
    public Query createNamedQuery(String s) {
        return delegate.createNamedQuery(s);
    }

    @Override
    public <T> TypedQuery<T> createNamedQuery(String s, Class<T> aClass) {
        return delegate.createNamedQuery(s, aClass);
    }

    @Override
    public Query createNativeQuery(String s) {
        return delegate.createNativeQuery(s);
    }

    @Override
    public Query createNativeQuery(String s, Class aClass) {
        return delegate.createNativeQuery(s, aClass);
    }

    @Override
    public Query createNativeQuery(String s, String s1) {
        return delegate.createNativeQuery(s, s1);
    }

    @Override
    public StoredProcedureQuery createNamedStoredProcedureQuery(String s) {
        return delegate.createNamedStoredProcedureQuery(s);
    }

    @Override
    public StoredProcedureQuery createStoredProcedureQuery(String s) {
        return delegate.createStoredProcedureQuery(s);
    }

    @Override
    public StoredProcedureQuery createStoredProcedureQuery(String s, Class... classes) {
        return delegate.createStoredProcedureQuery(s, classes);
    }

    @Override
    public StoredProcedureQuery createStoredProcedureQuery(String s, String... strings) {
        return delegate.createStoredProcedureQuery(s, strings);
    }

    @Override
    public void joinTransaction() {
        delegate.joinTransaction();
    }

    @Override
    public boolean isJoinedToTransaction() {
        return delegate.isJoinedToTransaction();
    }

    @Override
    public <T> T unwrap(Class<T> aClass) {
        return delegate.unwrap(aClass);
    }

    @Override
    public Object getDelegate() {
        return delegate.getDelegate();
    }

    @Override
    public void close() {
        delegate.close();
    }

    @Override
    public boolean isOpen() {
        return delegate.isOpen();
    }

    @Override
    public EntityTransaction getTransaction() {
        return delegate.getTransaction();
    }

    @Override
    public EntityManagerFactory getEntityManagerFactory() {
        return delegate.getEntityManagerFactory();
    }

    @Override
    public CriteriaBuilder getCriteriaBuilder() {
        return delegate.getCriteriaBuilder();
    }

    @Override
    public Metamodel getMetamodel() {
        return delegate.getMetamodel();
    }

    @Override
    public <T> EntityGraph<T> createEntityGraph(Class<T> aClass) {
        return delegate.createEntityGraph(aClass);
    }

    @Override
    public EntityGraph<?> createEntityGraph(String s) {
        return delegate.createEntityGraph(s);
    }

    @Override
    public EntityGraph<?> getEntityGraph(String s) {
        return delegate.getEntityGraph(s);
    }

    @Override
    public <T> List<EntityGraph<? super T>> getEntityGraphs(Class<T> aClass) {
        return delegate.getEntityGraphs(aClass);
    }
}
