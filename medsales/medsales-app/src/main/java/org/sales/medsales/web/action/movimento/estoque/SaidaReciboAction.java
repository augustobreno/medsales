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

	private String tituloRecibo;
	
	private String nomeCliente;
	
	public String getTituloRecibo() {
		return tituloRecibo;
	}

	public void setTituloRecibo(String tituloRecibo) {
		this.tituloRecibo = tituloRecibo;
	}

	@Override
	protected void postLoadId() {
		super.postLoadId();
		setTituloRecibo();
		setNomeCliente();
	}
	
	private void setNomeCliente() {
		String nome = "Cliente";
		if (getMovimentacao() != null && getMovimentacao().getParceiro() != null) {
			nome = getMovimentacao().getParceiro().getNome(); 
		}
		setNomeCliente(nome);
	}

	private void setTituloRecibo() {
		String titulo = "Pedido";
		if (getMovimentacao() != null && getMovimentacao().getNumeroPedido() != null) {
			titulo = "Pedido Nº " + getMovimentacao().getNumeroPedido(); 
		}
		setTituloRecibo(titulo);
	}

	public String getNomeCliente() {
		return nomeCliente;
	}

	public void setNomeCliente(String nomeCliente) {
		this.nomeCliente = nomeCliente;
	}

}
