package org.sales.medsales.web.action;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.easy.qbeasy.QBEFilter;
import org.easy.qbeasy.api.Filter;
import org.easy.qbeasy.api.OperationContainer.ContainerType;
import org.easy.qbeasy.api.operator.Operators;
import org.sales.medsales.api.util.StringUtil;
import org.sales.medsales.api.web.action.ActionBase;
import org.sales.medsales.dominio.Ciclo;
import org.sales.medsales.negocio.movimentacao.valor.CicloFacade;

/**
 * Oferce suporte aos componentes de interface que realizam consulta de ciclos. 
 * @author Augusto
 */
@SuppressWarnings("serial")
@Named
public class SearchCicloAction extends ActionBase {

	@Inject
	private CicloFacade cicloFacade;
	
	/**
	 * Consulta por ciclos segundo a chave informada.
	 * @param chave
	 *            Chave para consulta de ciclos.
	 * @return Lista de ciclos segundo a chave informada.
	 */
	public List<Ciclo> search(String chave) {
		Filter<Ciclo> filter = new QBEFilter<Ciclo>(new Ciclo());
		filter.setRootContainerType(ContainerType.OR);
		filter.filterBy("investidor.nome", Operators.like(false), chave);
		
		// filtra pelo código, se possível
		String numeros = StringUtil.extractNumbers(chave);
		if (numeros != null && !numeros.isEmpty()) {
			filter.filterBy("id", Operators.equal(), Long.parseLong(numeros));
		}
			
		return cicloFacade.findAllBy(filter);
	}	
	
}
