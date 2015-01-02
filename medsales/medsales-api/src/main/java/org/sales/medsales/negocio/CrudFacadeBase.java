package org.sales.medsales.negocio;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;

import javax.inject.Inject;

import org.easy.qbeasy.QBEFilter;
import org.easy.qbeasy.api.Filter;
import org.easy.qbeasy.api.operator.Operators;
import org.sales.medsales.dominio.Entity;
import org.sales.medsales.persistencia.repository.CrudRepository;
import org.sales.medsales.util.DIContainerUtil;
import org.sales.medsales.util.JavaGenericsUtil;
import org.sales.medsales.util.ReflectionUtil;

@SuppressWarnings("serial")
@BusinessExceptionHandler
public abstract class CrudFacadeBase<CR extends CrudRepository<ENTITY, PK>, ENTITY extends Entity<PK>, PK extends Serializable>
        implements CrudFacade<ENTITY, PK> {

    @Inject
    protected Logger log;
    
    private CR repository;

    @SuppressWarnings("unchecked")
	protected CrudRepository<ENTITY, PK> getRepository() {
        if (repository == null) {
            List<Class<?>> genericsTypedArguments = JavaGenericsUtil.getGenericTypedArguments(CrudFacadeBase.class, this.getClass());
            Class<CR> repositoryType = (Class<CR>) genericsTypedArguments.get(0); // o repostiório é o primeiro parãmetro genérico
            repository = new DIContainerUtil().lookup(repositoryType);
        }
        return repository;
    }

    @Override
    public ENTITY save(ENTITY entity) {
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
    public void remove(ENTITY entity) {
        getRepository().remove(entity);
    }

    @Override
    public ENTITY findBy(PK primaryKey) {
        return getRepository().findBy(primaryKey);
    }

    @Override
    public ENTITY findBy(Filter<ENTITY> filter) {
        return getRepository().findBy(filter);
    }
    
    @Override
    public List<ENTITY> findAllBy(Filter<ENTITY> filter) {
        return getRepository().findAllBy(filter);
    }
    
    @Override
    public List<ENTITY> findAll() {
        return getRepository().findAll();
    }

    @Override
    public Long count() {
        return getRepository().count();
    }
    
    @Override
    public Long count(Filter<ENTITY> filter) {
        return getRepository().count(filter);
    }

    /**
     * Ponto de extensão para realização de validação de RN antes da operação save().
     * Este método sempre será executado juntamente (imediatamente antes) com {@link #validateInsert(Entity)}
     * ou {@link #validateUpdate(Entity)} 
     * @param entity Entidade a ser validada.
     */
    protected void validateSave(ENTITY entity) {    }
    
    /**
     * Ponto de extensão para realização de validação de RN antes da operação save, 
     * quando esta realizar um insert da entidade.
     * @param entity Entidade a ser validada.
     */
    protected void validateInsert(ENTITY entity) {    }
    
    /**
     * Ponto de extensão para realização de validação de RN antes da operação save, 
     * quando esta realizar um update da entidade.
     * @param entity Entidade a ser validada.
     */
    protected void validateUpdate(ENTITY entity) {    }
    
	/**
	 * Verifica se já há um objeto cadastro com os dados de unicidade informados. Para o fluxo de inclusão,
	 * apenas verifica se há outro objeto com as mesmas informações. Para o fluxo de alteração, verifica se há outro objeto
	 * diferente deste com as mesmas informações.
	 * @param entity Entidade a ser validada.
	 * @param uniqueProperties Propriedades que, juntas, representam uma regra de unicidade.
	 * @param e Exception a ser lançada caso a unicidade seja desrespeitada.
	 * 
	 * @return true se existir outro registro com as mesmas propriedades de unicidade, false caso contrário. 
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected boolean checkUniqueConstraint(ENTITY entity, String...uniqueProperties) {
		QBEFilter<? extends Entity> filter = new QBEFilter<>(entity.getClass());
		
		if (entity.getId() != null) {
			// fluxo de alteração
			filter.filterBy("id", Operators.notEqual(), entity.getId());
		}
		
		for (String uniqueProp : uniqueProperties) {
			Object uniqueValue = ReflectionUtil.getValue(entity, uniqueProp);
			filter.filterBy(uniqueProp, Operators.equal(), uniqueValue);
		}
		
		Long count = getRepository().count((Filter<? extends ENTITY>) filter);
		return count == 0;
	}
    
}
