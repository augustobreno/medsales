package org.sales.medsales.web.action.movimento.estoque;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;

import javax.enterprise.context.ConversationScoped;
import javax.inject.Named;

import org.sales.medsales.dominio.movimento.estoque.SaidaEstoque;

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
	
	protected void setNomeCliente() {
		String nome = "<Nome do Parceiro>";
		if (getMovimentacao() != null && getMovimentacao().getParceiro() != null) {
			nome = getMovimentacao().getParceiro().getNome(); 
		}
		setNomeCliente(nome);
	}

	protected void setTituloRecibo() {
		
		SaidaEstoque movimento = getMovimentacao() != null ? getMovimentacao() : new SaidaEstoque(); 
		
		String format = "Pedido: {0} em {1} - Nº {2}";
		String titulo = 
				MessageFormat.format(format, 
					movimento.getParceiro() != null ?  movimento.getParceiro().getNome() : "<Parceiro>",
					movimento.getDataMovimento() != null ? new SimpleDateFormat("dd/MM/yyyy").format(movimento.getDataMovimento()) : "<Data>",
					movimento.getNumeroPedido() != null ?  movimento.getNumeroPedido() : "<Número>");
		
		setTituloRecibo(titulo);
	}

	public String getNomeCliente() {
		return nomeCliente;
	}

	public void setNomeCliente(String nomeCliente) {
		this.nomeCliente = nomeCliente;
	}

}
