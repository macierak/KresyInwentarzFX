package pl.vost.kresyinwentarzfx.persistence;

import jakarta.persistence.EntityManager;

import java.util.List;

public class BaseRepositoryImpl<T>{

    protected final EntityManager entityManager = DatabaseConnection.getEntityManager();
    private final Class<T> entityClass;

    public BaseRepositoryImpl(Class<T> entityClass){
        this.entityClass = entityClass;
    }

    public T save(T entity){
        entityManager.getTransaction().begin();
        entityManager.persist(entity);
        entityManager.getTransaction().commit();
        return entity;
    }

    public T update(T entity){
        entityManager.getTransaction().begin();
        entityManager.merge(entity);
        entityManager.getTransaction().commit();
        return entity;
    }

    public void delete(T entity){
        entityManager.getTransaction().begin();
        entityManager.remove(entity);
        entityManager.getTransaction().commit();
    }

    public T findById(Long id){
        return entityManager.find(entityClass, id);
    }

    public List<T> findAll(){
        return entityManager.createQuery("SELECT e FROM " + entityClass.getSimpleName() + " e", entityClass).getResultList();
    }

}
