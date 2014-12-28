package org.sales.medsales.persistencia.repository;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Id;
import javax.persistence.PersistenceContext;

import org.easy.qbeasy.QBEFilter;
import org.easy.qbeasy.api.Filter;
import org.easy.qbeasy.api.QBERepository;
import org.easy.qbeasy.api.SortConfig;
import org.easy.qbeasy.api.SortConfig.SortType;
import org.easy.qbeasy.api.operator.Operators;
import org.sales.medsales.dominio.Entity;
import org.sales.medsales.exceptions.AppException;
import org.sales.medsales.util.JavaGenericsUtil;
import org.sales.medsales.util.ReflectionUtil;

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

	@PersistenceContext
	private EntityManager em;
	
	@Inject
	private QBERepository qbeRepository;

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
	
	@Override
	public ENTITY findBy(PK id, String... fetch) {
		
		Field idField = getIdField(getEntityClass());
		
		Filter<ENTITY> filtro = new QBEFilter<ENTITY>(getEntityClass());
		filtro.filterBy(idField.getName(), Operators.equal(), id);
		
		// adicionando propriedades para fetch
		if (fetch != null) {
			for (String f : fetch) {
				if (f != null) {
					filtro.addFetch(f);
				}
			}
		}

		return findBy(filtro);
	}
	
	@Override
	public ENTITY findBy(Filter<? extends ENTITY> filter)
			throws NonUniqueEntityException {

		List<ENTITY> list = findAllBy(filter);

		if (list == null || list.isEmpty()) {
			return null;
		} else if (list.size() > 1) {
			throw new NonUniqueEntityException();
		}
		return list.get(0);
	}
	
	@Override
	public List<ENTITY> findAll(boolean ascedant, String... orderBy) {
		SortConfig.SortType tipoOrdenacao = ascedant ? SortType.ASCENDING
				: SortType.DESCENDING;

		QBEFilter<ENTITY> filtro = new QBEFilter<ENTITY>(getEntityClass());
		if (orderBy != null) {
			for (String prop : orderBy) {
				filtro.sortBy(new SortConfig(prop, tipoOrdenacao));
			}
		}

		return getQbeRepository().search(filtro);
	}
	
	@Override
	public Long count(Filter<? extends ENTITY> filter) {
		long count = getQbeRepository().count(filter);
		return count;
	}
	
	@Override
	public List<ENTITY> findAllBy(Filter<? extends ENTITY> filter) {
		return getQbeRepository().search(filter);
	}

	protected EntityManager getEm() {
		return em;
	}

	protected void setEm(EntityManager em) {
		this.em = em;
	}

	protected QBERepository getQbeRepository() {
		return qbeRepository;
	}

	protected void setQbeRepository(QBERepository qbeRepository) {
		this.qbeRepository = qbeRepository;
	}
	
}
