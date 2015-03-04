package org.sales.medsales.web.action.movimento.valor;

import javax.enterprise.context.ConversationScoped;
import javax.inject.Named;

import org.sales.medsales.dominio.movimento.valor.EntradaEstoqueValor;
import org.sales.medsales.negocio.movimentacao.valor.EntradaEstoqueValorFacade;

@SuppressWarnings("serial")
@Named
@ConversationScoped
public class EntradaEstoqueValorAction extends MovimentoValorActionBase<EntradaEstoqueValor, EntradaEstoqueValorFacade>{
	
	/**
	 * Evento disparado quando uma entrada é selecionada no combobox.
	 */
	public void onEntradaSelecionada() {
		
		if (getEntity().getEntradaEstoque() != null) {
			getEntity().setValor(getEntity().getEntradaEstoque().calcularTotal());
			getEntity().setDataMovimento(getEntity().getEntradaEstoque().getDataMovimento());
		} else {
			getEntity().setValor(null);
			getEntity().setDataMovimento(null);
		}
		
	}
}
