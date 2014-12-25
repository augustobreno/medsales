package org.sales.medsales.negocio;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;

import javax.inject.Inject;

import org.sales.medsales.dominio.Entity;
import org.sales.medsales.persistencia.repository.CrudRepository;
import org.sales.medsales.util.DIContainerUtil;
import org.sales.medsales.util.JavaGenericsUtil;

@SuppressWarnings("serial")
@BusinessExceptionHandler
public abstract class CrudFacadeBase<CR extends CrudRepository<E, PK>, E extends Entity<PK>, PK extends Serializable>
        implements CrudFacade<E, PK> {

    @Inject
    protected Logger log;
    
    private CR repository;

    @SuppressWarnings("unchecked")
	protected CrudRepository<E, PK> getRepository() {
        if (repository == null) {
            List<Class<?>> genericsTypedArguments = JavaGenericsUtil.getGenericTypedArguments(CrudFacadeBase.class, this.getClass());
            Class<CR> repositoryType = (Class<CR>) genericsTypedArguments.get(0); // o repostiório é o primeiro parãmetro genérico
            repository = new DIContainerUtil().lookup(repositoryType);
        }
        return repository;
    }

    @Override
    public E save(E entity) {
    	validateSave(entity);
		if (entity.getId() == null) {
			validateInsert(entity);
			entity = getRepository().insert(entity);
		} else {
			validateUpdate(entity);
			entity = getRepository().update(entity);
		}
		return entity;
    }

    @Override
    public void remove(E entity) {
        getRepository().remove(entity);
    }

    @Override
    public E findBy(PK primaryKey) {
        return getRepository().findBy(primaryKey);
    }

    @Override
    public List<E> findAll() {
        return getRepository().findAll();
    }

    @Override
    public Long count() {
        return getRepository().count();
    }

    /**
     * Ponto de extensão para realização de validação de RN antes da operação save().
     * Este método sempre será executado juntamente (imediatamente antes) com {@link #validateInsert(Entity)}
     * ou {@link #validateUpdate(Entity)} 
     * @param entity Entidade a ser validada.
     */
    protected void validateSave(E entity) {    }
    
    /**
     * Ponto de extensão para realização de validação de RN antes da operação save, 
     * quando esta realizar um insert da entidade.
     * @param entity Entidade a ser validada.
     */
    protected void validateInsert(E entity) {    }
    
    /**
     * Ponto de extensão para realização de validação de RN antes da operação save, 
     * quando esta realizar um update da entidade.
     * @param entity Entidade a ser validada.
     */
    protected void validateUpdate(E entity) {    }
    
    
}
