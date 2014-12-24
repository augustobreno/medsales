package com.sales.medsales.persistencia.repository;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Id;

import com.sales.medsales.dominio.Entity;
import com.sales.medsales.exceptions.AppException;
import com.sales.medsales.util.JavaGenericsUtil;
import com.sales.medsales.util.ReflectionUtil;

/**
 * Querier Object para entidades de domínio. Expõe apenas operações para
 * recuperação de dados.
 * 
 * @author augusto
 *
 */
public class CrudRepositoryBase<ENTITY extends Entity<PK>, PK extends Serializable>
		implements CrudRepository<ENTITY, PK> {

	@Inject
	protected Logger log;

	@Inject
	private EntityManager em;

	private Class<? extends ENTITY> entityClass;

	@Override
	public void remove(ENTITY entity) {
		if (!em.contains(entity)) {
			log.log(Level.FINE, "Merging entidade no Entitymanager antes de remover");
			log.log(Level.FINER, entity.toString());
			entity = em.merge(entity);
		}
		em.remove(entity);
		em.flush();
	}

	/**
	 * @return O tipo da entidade associada a este
	 */
	@SuppressWarnings("unchecked")
	protected Class<? extends ENTITY> getEntityClass() {
		if (this.entityClass == null) {
			List<Class<?>> typeArguments = JavaGenericsUtil
					.getGenericTypedArguments(CrudRepositoryBase.class,
							this.getClass());
			this.entityClass = (Class<? extends ENTITY>) typeArguments.get(0);
		}

		return this.entityClass;
	}

	/**
	 * Recupera o campo que representa o ID da entidade.
	 * 
	 * @param clazz
	 *            Tipo da entidade.
	 * @return Campo ID da entidade.
	 */
	@SuppressWarnings("rawtypes")
	protected Field getIdField(Class<? extends Entity> clazz) {
		List<Field> idFields = ReflectionUtil.getFields(clazz, Id.class);
		if (idFields == null || idFields.size() != 1) {
			throw new AppException(
					"Não foi possível identificar um Id para a entidade "
							+ clazz.getSimpleName());
		}

		return idFields.get(0);
	}

	@Override
	public ENTITY findBy(PK pk) {
		return getEm().find(getEntityClass(), pk);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ENTITY> findAll() {
		return getEm().createQuery("from " + getEntityClass().getSimpleName()).getResultList();
	}
	
	@Override
	public Long count() {
		return (Long) getEm().createQuery("select count(e) from " + getEntityClass().getSimpleName() + " e").getSingleResult();
	}

	@Override
	public ENTITY insert(ENTITY entity) {
		em.persist(entity);
		em.flush();
		return entity;
	}
	
	@Override
	public ENTITY update(ENTITY entity) {
		entity = em.merge(entity);
		em.flush();
		return entity;
	}

	protected EntityManager getEm() {
		return em;
	}

	protected void setEm(EntityManager em) {
		this.em = em;
	}
	
}
