package org.sales.medsales.web.action;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.easy.qbeasy.QBEFilter;
import org.easy.qbeasy.api.Filter;
import org.easy.qbeasy.api.OperationContainer.ContainerType;
import org.easy.qbeasy.api.operator.Operators;
import org.sales.medsales.api.util.StringUtil;
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
		
		if (StringUtil.isStringEmpty(chave)) {
			chave = null;
		}
		
		Filter<MovimentoEstoque> filter = new QBEFilter<MovimentoEstoque>(EntradaEstoque.class);
		filter.setRootContainerType(ContainerType.OR);
		filter.paginate(0, 10); // para otimização	
		
		filter.addFetch("parceiro", "itens.precoProduto");
		filter.sortDescBy("dataMovimento", "id");
		
		filter.filterBy("parceiro.nome", Operators.like(false), chave);
		
		// filtra pelo código, se possível
		String numeros = StringUtil.extractNumbers(chave);
		if (numeros != null && !numeros.isEmpty()) {
			filter.filterBy("id", Operators.equal(), Long.parseLong(numeros));
		}
		
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
