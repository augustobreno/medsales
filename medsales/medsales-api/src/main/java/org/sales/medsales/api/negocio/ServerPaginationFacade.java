package org.sales.medsales.api.negocio;

import java.io.Serializable;
import java.util.List;

import org.easy.qbeasy.api.Filter;
import org.sales.medsales.api.dominio.Entity;

public interface ServerPaginationFacade<ENTITY extends Entity<PK>, PK extends Serializable> extends Facade {

	/**
	 * COnsulta todos os registros de uma determinada entidade baseado 
	 * nas registrões configuradas no filtro.
	 */
	List<ENTITY> findAllBy(Filter<ENTITY> filter);
	
	/**
	 * Conta todos os registro de uma determinada entidade que atendem
	 * às restrições configuradas no filtro. 
	 */
	Long count(Filter<ENTITY> filter);
}
