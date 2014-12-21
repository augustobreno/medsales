package com.sales.medsales.negocio;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import org.apache.logging.log4j.Logger;

import br.jus.trt.lib.qbe.api.Filter;

import com.sales.medsales.dominio.Entity;
import com.sales.medsales.persistencia.CrudRepository;
import com.sales.medsales.util.DIContainerUtil;
import com.sales.medsales.util.JavaGenericsUtil;

@BusinessExceptionHandler
public abstract class CrudFacadeBase<CR extends CrudRepository<E, PK>, E extends Entity<PK>, PK extends Serializable>
        implements CrudFacade<E, PK> {

    @Inject
    protected Logger log;
    
    private CR repository;

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
        return getRepository().save(entity);
    }

    @Override
    public void remove(E entity) {
        log.entry();
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

    @Override
    public E findBy(PK id, String... fetch) {
        return getRepository().findBy(id, fetch);
    }

    @Override
    public E findBy(Filter<? extends E> filter) {
        return getRepository().findBy(filter);
    }

    @Override
    public List<E> findAll(boolean ascedant, String... orderBy) {
        return getRepository().findAll(ascedant, orderBy);
    }

    @Override
    public Long count(Filter<? extends E> filter) {
        return getRepository().count(filter);
    }

    @Override
    public List<E> findAllBy(Filter<? extends E> filter) {
        return getRepository().findAllBy(filter);
    }

}
