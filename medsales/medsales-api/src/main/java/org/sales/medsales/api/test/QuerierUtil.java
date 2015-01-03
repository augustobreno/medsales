package org.sales.medsales.api.test;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.sales.medsales.api.dominio.Entity;

/**
 * Oferece operações genéricas de consulta para testes unitários.
 * @author Augusto
 *
 */
public class QuerierUtil {

	@Inject
	private EntityManager em;
	
	/**
	 * @param type Tipo da entidade a ser retornada.
	 * @return Qualquer entidade do tipo informado.
	 */
	@SuppressWarnings("unchecked")
	public <T extends Entity<?>> T findAny(Class<T> type) {
		Query query = getEm().createQuery("from " + type.getSimpleName());
		query.setMaxResults(1);
		return (T) query.getSingleResult();
	}
	
	/**
	 * @param type Tipo da entidade a ser retornada.
	 * @param position Posição do registro a ser recuperado (zero based).
	 * @return Qualquer entidade do tipo informado.
	 */
	@SuppressWarnings("unchecked")
	public <T extends Entity<?>> T findAt(Class<T> type, int position) {
		Query query = getEm().createQuery("from " + type.getSimpleName());
		query.setMaxResults(1);
		query.setFirstResult(position);
		return (T) query.getSingleResult();
	}
	
	/**
	 * @param type Tipo da entidade a ser retornada.
	 * @return Todas os objetos do tipo informado.
	 */
	@SuppressWarnings("unchecked")
	public <T extends Entity<?>> List<T> findAll(Class<T> type) {
		Query query = getEm().createQuery("from " + type.getSimpleName());
		return query.getResultList();
	}

	/**
	 * 
	 * @param type TIpo da entidade a ser consultada.
	 * @return Número total de registros encontrados na base de dados.
	 */
	public <T extends Entity<?>> Long count(Class<T> type) {
		return (Long) getEm().createQuery("select count(e) from " + type.getSimpleName() + " e ").getSingleResult();
	}
	
	public EntityManager getEm() {
		return em;
	}

	public void setEm(EntityManager em) {
		this.em = em;
	}
}
