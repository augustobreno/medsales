package org.sales.medsales.web.action.movimento.estoque;

import javax.enterprise.context.ConversationScoped;
import javax.inject.Named;

/**
 * Para controle da tela de impressão de recibo de saída de estoque.
 * @author Augusto
 */
@SuppressWarnings("serial")
@Named
@ConversationScoped
public class SaidaReciboAction extends SaidaAction {

	private String nomeCliente;
	
	@Override
	protected void postLoadId() {
		super.postLoadId();
		setNomeCliente();
	}
	
	protected void setNomeCliente() {
		String nome = "<Nome do Parceiro>";
		if (getMovimentacao() != null && getMovimentacao().getParceiro() != null) {
			nome = getMovimentacao().getParceiro().getNome(); 
		}
		setNomeCliente(nome);
	}

	public String getNomeCliente() {
		return nomeCliente;
	}

	public void setNomeCliente(String nomeCliente) {
		this.nomeCliente = nomeCliente;
	}

}
