package org.sales.medsales.api.negocio;

import java.io.Serializable;
import java.util.List;

import org.easy.qbeasy.api.Filter;
import org.sales.medsales.api.dominio.Entity;

public interface CrudFacade<ENTITY extends Entity<PK>, PK extends Serializable> extends ServerPaginationFacade<ENTITY, PK> {

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
    ENTITY save(ENTITY entity);

    /**
     * Convenience access to
     * {@link javax.persistence.EntityManager#remove(Object)}.
     *
     * @param entity Entity to remove.
     */
    void remove(ENTITY entity);

    /**
     * Entity lookup by primary key. Convenicence method around
     * {@link javax.persistence.EntityManager#find(Class, Object)}.
     *
     * @param primaryKey DB primary key.
     * @return Entity identified by primary or null if it does not exist.
     */
    ENTITY findBy(PK primaryKey);
    
    /**
     * Entity lookup by primary key. Convenicence method around
     * {@link javax.persistence.EntityManager#find(Class, Object)}.
     *
     * @param primaryKey DB primary key.
     * @param fetch Associações apra carregamento.
     * @return Entity identified by primary or null if it does not exist.
     */
    ENTITY findBy(PK primaryKey, String...fetch);
    
    /**
     * Lookup all existing entities of entity class {@code <E extends Entity>}.
     *
     * @return List of entities, empty if none found.
     */
    List<ENTITY> findAll();

    /**
     * Conta todas os registros de uma determinada entidade.
     */
    Long count();

    /**
     * Encontra um registro de uma determinada entidade. O filtro permitirá configurar
     * como as associações deverão ser carregadas, além de permitir a configuração
     * de restrições para busca além da PK. O resultado deverá ser um único registro.
     */
	ENTITY findBy(Filter<ENTITY> filter);

}
