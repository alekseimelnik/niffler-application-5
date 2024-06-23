package guru.qa.niffler.data.jpa;

import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaDelete;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.CriteriaUpdate;
import jakarta.persistence.metamodel.Metamodel;

import java.util.List;
import java.util.Map;

public class ThreadSafeEntityManager implements EntityManager {

    private final EntityManagerFactory emf;
    private final ThreadLocal<EntityManager> managerThreadLocal = new ThreadLocal<>();

    private EntityManager emTl() {
        if (managerThreadLocal.get() == null) {
            managerThreadLocal.set(emf.createEntityManager());
        }
        return managerThreadLocal.get();
    }

    public ThreadSafeEntityManager(EntityManager delegate) {
        this.emf = delegate.getEntityManagerFactory();
        this.managerThreadLocal.set(emTl());
    }

    @Override
    public void close() {
        if (managerThreadLocal.get() != null) {
            managerThreadLocal.get().close();
            managerThreadLocal.set(null);
        }
    }

    @Override
    public void persist(Object entity) {
        emTl().persist(entity);
    }

    @Override
    public <T> T merge(T entity) {
        return emTl().merge(entity);
    }

    @Override
    public void remove(Object entity) {
        emTl().remove(entity);
    }

    @Override
    public <T> T find(Class<T> aClass, Object entity) {
        return emTl().find(aClass, entity);
    }

    @Override
    public <T> T find(Class<T> aClass, Object entity, Map<String, Object> map) {
        return emTl().find(aClass, entity, map);
    }

    @Override
    public <T> T find(Class<T> aClass, Object entity, LockModeType lockModeType) {
        return emTl().find(aClass, entity, lockModeType);
    }

    @Override
    public <T> T find(Class<T> aClass, Object entity, LockModeType lockModeType, Map<String, Object> map) {
        return emTl().find(aClass, entity, lockModeType, map);
    }

    @Override
    public <T> T getReference(Class<T> aClass, Object entity) {
        return emTl().getReference(aClass, entity);
    }

    @Override
    public void flush() {
        emTl().flush();
    }

    @Override
    public void setFlushMode(FlushModeType flushModeType) {
        emTl().setFlushMode(flushModeType);
    }

    @Override
    public FlushModeType getFlushMode() {
        return emTl().getFlushMode();
    }

    @Override
    public void lock(Object entity, LockModeType lockModeType) {
        emTl().lock(entity, lockModeType);
    }

    @Override
    public void lock(Object entity, LockModeType lockModeType, Map<String, Object> map) {
        emTl().lock(entity, lockModeType, map);
    }

    @Override
    public void refresh(Object entity) {
        emTl().refresh(entity);
    }

    @Override
    public void refresh(Object entity, Map<String, Object> map) {
        emTl().refresh(entity, map);
    }

    @Override
    public void refresh(Object entity, LockModeType lockModeType) {
        emTl().refresh(entity, lockModeType);
    }

    @Override
    public void refresh(Object entity, LockModeType lockModeType, Map<String, Object> map) {
        emTl().refresh(entity, lockModeType, map);
    }

    @Override
    public void clear() {
        emTl().clear();
    }

    @Override
    public void detach(Object entity) {
        emTl().detach(entity);
    }

    @Override
    public boolean contains(Object entity) {
        return emTl().contains(entity);
    }

    @Override
    public LockModeType getLockMode(Object entity) {
        return emTl().getLockMode(entity);
    }

    @Override
    public void setProperty(String s, Object entity) {
        emTl().setProperty(s, entity);
    }

    @Override
    public Map<String, Object> getProperties() {
        return emTl().getProperties();
    }

    @Override
    public Query createQuery(String s) {
        return emTl().createQuery(s);
    }

    @Override
    public <T> TypedQuery<T> createQuery(CriteriaQuery<T> criteriaQuery) {
        return emTl().createQuery(criteriaQuery);
    }

    @Override
    public Query createQuery(CriteriaUpdate criteriaUpdate) {
        return emTl().createQuery(criteriaUpdate);
    }

    @Override
    public Query createQuery(CriteriaDelete criteriaDelete) {
        return emTl().createQuery(criteriaDelete);
    }

    @Override
    public <T> TypedQuery<T> createQuery(String s, Class<T> aClass) {
        return emTl().createQuery(s, aClass);
    }

    @Override
    public Query createNamedQuery(String s) {
        return emTl().createNamedQuery(s);
    }

    @Override
    public <T> TypedQuery<T> createNamedQuery(String s, Class<T> aClass) {
        return emTl().createNamedQuery(s, aClass);
    }

    @Override
    public Query createNativeQuery(String s) {
        return emTl().createNativeQuery(s);
    }

    @Override
    public Query createNativeQuery(String s, Class aClass) {
        return emTl().createNativeQuery(s, aClass);
    }

    @Override
    public Query createNativeQuery(String s, String s1) {
        return emTl().createNativeQuery(s, s1);
    }

    @Override
    public StoredProcedureQuery createNamedStoredProcedureQuery(String s) {
        return emTl().createNamedStoredProcedureQuery(s);
    }

    @Override
    public StoredProcedureQuery createStoredProcedureQuery(String s) {
        return emTl().createStoredProcedureQuery(s);
    }

    @Override
    public StoredProcedureQuery createStoredProcedureQuery(String s, Class... classes) {
        return emTl().createStoredProcedureQuery(s, classes);
    }

    @Override
    public StoredProcedureQuery createStoredProcedureQuery(String s, String... strings) {
        return emTl().createStoredProcedureQuery(s, strings);
    }

    @Override
    public void joinTransaction() {
        emTl().joinTransaction();
    }

    @Override
    public boolean isJoinedToTransaction() {
        return emTl().isJoinedToTransaction();
    }

    @Override
    public <T> T unwrap(Class<T> aClass) {
        return emTl().unwrap(aClass);
    }

    @Override
    public Object getDelegate() {
        return emTl().getDelegate();
    }

    @Override
    public boolean isOpen() {
        return emTl().isOpen();
    }

    @Override
    public EntityTransaction getTransaction() {
        return emTl().getTransaction();
    }

    @Override
    public EntityManagerFactory getEntityManagerFactory() {
        return emTl().getEntityManagerFactory();
    }

    @Override
    public CriteriaBuilder getCriteriaBuilder() {
        return emTl().getCriteriaBuilder();
    }

    @Override
    public Metamodel getMetamodel() {
        return emTl().getMetamodel();
    }

    @Override
    public <T> EntityGraph<T> createEntityGraph(Class<T> aClass) {
        return emTl().createEntityGraph(aClass);
    }

    @Override
    public EntityGraph<?> createEntityGraph(String s) {
        return emTl().createEntityGraph(s);
    }

    @Override
    public EntityGraph<?> getEntityGraph(String s) {
        return emTl().getEntityGraph(s);
    }

    @Override
    public <T> List<EntityGraph<? super T>> getEntityGraphs(Class<T> aClass) {
        return emTl().getEntityGraphs(aClass);
    }
}
