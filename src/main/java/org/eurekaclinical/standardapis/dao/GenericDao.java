package org.eurekaclinical.standardapis.dao;

/*-
 * #%L
 * Eureka! Clinical Standard APIs
 * %%
 * Copyright (C) 2016 Emory University
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.SingularAttribute;

import javax.inject.Provider;
import javax.persistence.NoResultException;

/**
 * Generic implementation of the {@link Dao} interface. It is a wrapper around
 * the {@link EntityManager} interface.
 *
 * @param <T> The type of the entity.
 * @param <PK> The type of the unique identifier for the entity.
 * @author hrathod
 */
public class GenericDao<T, PK> implements Dao<T, PK> {

    /**
     * The type for the entities this DAO instance handles.
     */
    final Class<T> entityClass;

    /**
     * Provides entity managers on demand.
     */
    private final Provider<EntityManager> managerProvider;

    private final DatabaseSupport databaseSupport;

    /**
     * Creates a generic DAO that handles the given type of entity. The entity
     * manager provider is used to fetch entity managers to interact with the
     * data store.
     *
     * @param inEntityClass The type of entities to handle.
     * @param inManagerProvider Provides entity managers on demand.
     */
    protected GenericDao(Class<T> inEntityClass,
            Provider<EntityManager> inManagerProvider) {
        this.entityClass = inEntityClass;
        this.managerProvider = inManagerProvider;
        this.databaseSupport = new DatabaseSupport(this.managerProvider);
    }

    /**
     * Creates an entity in the database. It calls 
     * {@link EntityManager#persist(java.lang.Object) } and throws the same
     * exceptions.
     *
     * @param entity the entity to create.
     *
     * @return the created entity with any primary key field populated.
     *
     */
    @Override
    public T create(T entity) {
        EntityManager entityManager = getEntityManager();
        entityManager.persist(entity);
        return entity;
    }

    /**
     * Selects an entity from the database. It calls
     * {@link EntityManager#find(java.lang.Class, java.lang.Object) } and throws
     * the same exceptions.
     *
     * @param uniqueId the primary key of the entity to select.
     *
     * @return the entity, or <code>null</code> if none has the specified
     * primary key.
     */
    @Override
    public final T retrieve(PK uniqueId) {
        return getEntityManager().find(getEntityClass(), uniqueId);
    }

    /**
     * Updates an entity in the database. It calls
     * {@link EntityManager#merge(java.lang.Object) } and throws the same
     * exceptions.
     *
     * @param entity the updates to the entity.
     *
     * @return the updated entity.
     */
    @Override
    public T update(T entity) {
        EntityManager entityManager = getEntityManager();
        T result = entityManager.merge(entity);
        return result;
    }

    /**
     * Deletes an entity from the database. It calls
     * {@link EntityManager#remove(java.lang.Object) } and throws the same
     * exceptions.
     *
     * @param entity the entity to delete.
     *
     * @return the deleted entity.
     */
    @Override
    public T remove(T entity) {
        EntityManager entityManager = getEntityManager();
        if (entityManager.contains(entity)) {
            entityManager.remove(entity);
        } else {
            entityManager.remove(entityManager.merge(entity));
        }
        return entity;
    }

    /**
     * Synchronizes the given entity with what is currently in the database.
     *
     * @param entity the entity to refresh.
     *
     * @return the refreshed entity.
     */
    @Override
    public T refresh(T entity) {
        this.getEntityManager().refresh(entity);
        return entity;
    }

    /**
     * Gets all of this DAO's entities.
     *
     * @return a list of entities. Guaranteed not <code>null</code>.
     */
    @Override
    public List<T> getAll() {
        return getDatabaseSupport().getAll(getEntityClass());
    }
    
    /**
     * Gets all of this DAO's entities.
     *
     * @return a list of entities. Guaranteed not <code>null</code>.
     */
    @Override
    public List<T> getAll(int firstResult, int maxResults) {
        return getDatabaseSupport().getAll(getEntityClass(), firstResult, maxResults);
    }

    /**
     * Gets all of this DAO's entities ordered by the provided attribute in
     * ascending order.
     *
     * @param attribute the attribute to order by.
     *
     * @return an ordered list of entities. Guaranteed not <code>null</code>.
     */
    protected List<T> getListAsc(SingularAttribute<T, ?> attribute) {
        EntityManager entityManager = this.getEntityManager();
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = builder.createQuery(getEntityClass());
        Root<T> root = criteriaQuery.from(getEntityClass());
        criteriaQuery.orderBy(builder.asc(root.get(attribute)));
        return entityManager.createQuery(criteriaQuery).getResultList();
    }
    
    /**
     * Gets all of this DAO's entities ordered by the provided attribute in
     * ascending order.
     *
     * @param attribute the attribute to order by.
     *
     * @return an ordered list of entities. Guaranteed not <code>null</code>.
     */
    protected List<T> getListAsc(SingularAttribute<T, ?> attribute, int firstResult, int maxResults) {
        EntityManager entityManager = this.getEntityManager();
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = builder.createQuery(getEntityClass());
        Root<T> root = criteriaQuery.from(getEntityClass());
        criteriaQuery.orderBy(builder.asc(root.get(attribute)));
        return entityManager.createQuery(criteriaQuery)
                .setFirstResult(firstResult)
                .setMaxResults(maxResults)
                .getResultList();
    }

    /**
     * Gets the entity that has the target value of the specified attribute.
     *
     * @param <Y> the type of the attribute and target value.
     * @param attribute the attribute of the entity to compare.
     * @param value the target value of the given attribute.
     *
     * @return the matching entity, or <code>null</code> if there is none.
     */
    protected <Y> T getUniqueByAttribute(SingularAttribute<T, Y> attribute,
            Y value) {
        try {
            return getDatabaseSupport().getUniqueByAttribute(getEntityClass(), attribute, value);
        } catch (NoResultException ex) {
            return null;
        }
    }

    /**
     * Gets the entity that has the target value of the specified attribute.
     *
     * @param <Y> the type of the attribute and target value.
     * @param attributeName the name of the attribute.
     * @param value the target value of the given attribute.
     *
     * @return the matching entity, or <code>null</code> if there is none.
     */
    protected <Y> T getUniqueByAttribute(String attributeName, Y value) {
        try {
            return getDatabaseSupport().getUniqueByAttribute(getEntityClass(), attributeName, value);
        } catch (NoResultException ex) {
            return null;
        }
    }

    /**
     * Gets the entities that have the target value of the specified attribute.
     *
     * @param <Y> the type of the attribute and target value.
     * @param attribute the attribute of the entity to compare.
     * @param value the target value of the given attribute.
     *
     * @return the matching entities. Guaranteed not <code>null</code>.
     */
    protected <Y> List<T> getListByAttribute(SingularAttribute<T, Y> attribute, Y value) {
        return getDatabaseSupport().getListByAttribute(getEntityClass(), attribute, value);
    }

    /**
     * Executes a query for all entities whose path value is the same as the
     * given target value. The path is provided by the {@link QueryPathProvider}
     * and is followed through to get the resulting value. That resulting value
     * is compared to the given target value in the query.
     *
     * @param provider provides the path from the entity to the target
     * attribute/column.
     * @param value the target value to compare with the resulting attribute
     * value.
     * @param <Y> the type of the target value and resulting attribute/column
     * value.
     *
     * @return a list of entities that match the given criteria.
     */
    protected <Y> List<T> getListByAttribute(QueryPathProvider<T, Y> provider, Y value) {
        return getDatabaseSupport().getListByAttribute(getEntityClass(), provider, value);
    }

    /**
     * Gets the entities that have any of the target values of the specified
     * attribute.
     *
     * @param <Y> the type of the attribute and target value.
     * @param attribute the attribute of the entity to compare.
     * @param values the target values of the given attribute.
     *
     * @return the matching entities. Guaranteed not <code>null</code>.
     */
    protected <Y> List<T> getListByAttributeIn(SingularAttribute<T, Y> attribute, List<Y> values) {
        return getDatabaseSupport().getListByAttributeIn(getEntityClass(), attribute, values);
    }

    /**
     * Executes a query for all entities whose path value is any of the target
     * values. The path is provided by the {@link QueryPathProvider} and is
     * followed through to get the resulting value. That resulting value is
     * compared to the given target values in the query.
     *
     * @param <Y> the type of the target value and resulting attribute/column
     * value.
     * @param provider provides the path from the entity to the target
     * attribute/column.
     * @param values the target values of the given attribute.
     *
     * @return A list of entities that match the given criteria.
     */
    protected <Y> List<T> getListByAttributeIn(QueryPathProvider<T, Y> provider, List<Y> values) {
        return getDatabaseSupport().getListByAttributeIn(getEntityClass(), provider, values);
    }

    /**
     * Returns an entity manager that can be used to interact with the data
     * source.
     *
     * @return the entity manager.
     */
    protected EntityManager getEntityManager() {
        return this.managerProvider.get();
    }
    
    protected DatabaseSupport getDatabaseSupport() {
        return this.databaseSupport;
    }
    
    protected Class<T> getEntityClass() {
        return this.entityClass;
    }

}
