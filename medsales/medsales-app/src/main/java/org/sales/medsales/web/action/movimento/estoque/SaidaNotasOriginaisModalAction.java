package org.sales.medsales.web.action.movimento.estoque;

import java.math.BigDecimal;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.sales.medsales.api.web.action.ActionBase;
import org.sales.medsales.dominio.movimento.estoque.NotaCompra;
import org.sales.medsales.dominio.movimento.estoque.SaidaEstoque;

/**
 * MB para dar suporte ao cadastro dos valores das notas originais em uma saída.
 * @author Augusto
 *
 */
@SuppressWarnings("serial")
@Named
@ConversationScoped
public class SaidaNotasOriginaisModalAction extends ActionBase{

	@Inject
	private Conversation conversation;
	
	@Override
	@PostConstruct
	public void init() {
		super.init();
		if (conversation.isTransient()) {
			conversation.begin();
		}
	}	
	
	/**
	 * Para receber um novo valor de uma nota.
	 */
	private BigDecimal valor;

	/**
	 * Adiciona o valor encontrado em {@link #valor} como uma nota de compra original.
	 * @param saidaEstoque
	 * @see NotaCompra
	 */
	public void add(SaidaEstoque saidaEstoque) {
		if (this.valor != null) {
			saidaEstoque.addValorNota(valor);
			setValor(null);
		}
	}
	
	/**
	 * Remove uma nota da lista de notas armazenada.
	 * @param saidaEstoque SaidaEstoque que mantém as notas de compra.
	 * @param nota Nota de compra a ser removida.
	 */
	public void remove(SaidaEstoque saidaEstoque, NotaCompra nota) {
		saidaEstoque.removeValorNota(nota);
	}
	
	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}
	
}
