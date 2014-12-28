package org.sales.medsales.negocio;

import java.io.Serializable;
import java.util.List;

import org.sales.medsales.dominio.Entity;

public interface CrudFacade<E extends Entity<PK>, PK extends Serializable> extends Facade {

    /**
     * Persist (new entity) or merge the given entity. The distinction on
     * calling either method is done based on the primary key field being null
     * or not. If this results in wrong behavior for a specific case, consider
     * using the {@link org.apache.deltaspike.data.api.EntityManagerDelegate}
     * which offers both {@code persist} and {@code merge}.
     *
     * @param entity Entity to save.
     * @return Returns the modified entity.
     */
    E save(E entity);

    /**
     * Convenience access to
     * {@link javax.persistence.EntityManager#remove(Object)}.
     *
     * @param entity Entity to remove.
     */
    void remove(E entity);

    /**
     * Entity lookup by primary key. Convenicence method around
     * {@link javax.persistence.EntityManager#find(Class, Object)}.
     *
     * @param primaryKey DB primary key.
     * @return Entity identified by primary or null if it does not exist.
     */
    E findBy(PK primaryKey);

    /**
     * Lookup all existing entities of entity class {@code <E extends Entity>}.
     *
     * @return List of entities, empty if none found.
     */
    List<E> findAll();

    /**
     * Count all existing entities of entity class {@code <E extends Entity>}.
     *
     * @return Counter.
     */
    Long count();

}
