package org.sales.medsales.web.action;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.easy.qbeasy.QBEFilter;
import org.easy.qbeasy.api.Filter;
import org.sales.medsales.api.web.action.ActionBase;
import org.sales.medsales.dominio.movimento.estoque.EntradaEstoque;
import org.sales.medsales.dominio.movimento.estoque.MovimentoEstoque;
import org.sales.medsales.negocio.movimentacao.estoque.EstoqueFacade;

/**
 * Oferce suporte aos componentes de interface que realizam consulta de entradas em estoque. 
 * @author Augusto
 */
@SuppressWarnings("serial")
@Named
public class SearchEntradaEstoqueAction extends ActionBase {

	@Inject
	private EstoqueFacade estoqueFacade;
	
	/**
	 * Consulta por parceiros segundo a chave informada.
	 * @param chave
	 *            Chave para consulta de parceiros.
	 * @return Lista de parceiros segundo a chave informada.
	 */
	public List<EntradaEstoque> search(String chave) {
		Filter<MovimentoEstoque> filter = new QBEFilter<MovimentoEstoque>(new EntradaEstoque());
		filter.addFetch("parceiro");
		List<MovimentoEstoque> movimentos = estoqueFacade.findAllBy(filter);
		return parseToEntradaEstoque(movimentos);
	}

	private List<EntradaEstoque> parseToEntradaEstoque(List<MovimentoEstoque> movimentos) {
		
		List<EntradaEstoque> entradas = new ArrayList<EntradaEstoque>();
		
		if (movimentos != null) {
			for (MovimentoEstoque movimento : movimentos) {
				entradas.add((EntradaEstoque) movimento);
			}
		}
		
		return entradas;
	}	
	
}
