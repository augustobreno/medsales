package org.sales.medsales.api.web.action.pagination;

import org.easy.qbeasy.api.Filter;
import org.sales.medsales.api.dominio.Entity;
import org.sales.medsales.api.web.action.ResultData;

/**
 * Define as operações mínimas necessárias para que o {@link LazyEntityDataModel} tenha acesso
 * à infraestrutura do portador utilizada na construção e execução das consultas realizadas.
 * @author augusto
 */
public interface LazyEntityProvider<ENTITY extends Entity<?>> {

	/**
	 * @return Tipo da entidade associada a este provider.
	 */
	public Class<ENTITY> getEntityType();
	
	/**
	 * Realiza a consulta de objetos. Naturalmente, esta consulta deve ser baseada nos mínimos
	 * configurados no {@link Filter} informado.
	 * 
	 * @param minimalFilter
	 *            Filtro com pré-configuração mínima inicial para consulta.
	 */
	public ResultData<ENTITY> search(Filter<ENTITY> minimalFilter);	
	
}
