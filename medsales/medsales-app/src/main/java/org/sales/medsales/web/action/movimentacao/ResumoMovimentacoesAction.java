package org.sales.medsales.web.action.movimentacao;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.easy.qbeasy.QBEFilter;
import org.easy.qbeasy.api.Filter;
import org.sales.medsales.dominio.movimentacao.MovimentacaoEstoque;
import org.sales.medsales.negocio.EstoqueFacade;

import java.io.Serializable;
import java.util.List;

/**
 * Monta os dados da tela de resumo das últimas movimentações realizadas.
 * @author Augusto
 */
@SuppressWarnings("serial")
@Named
@ConversationScoped
public class ResumoMovimentacoesAction implements Serializable {

	@Inject
	private EstoqueFacade estoqueFacade;
	
	private List<MovimentacaoEstoque> movimentacoes;

	@PostConstruct
	private void onInit() {
		carregarUltimosMovimentos();
	}

	/**
	 * Consulta movimentos da última semana.
	 */
	private void carregarUltimosMovimentos() {
		movimentacoes = estoqueFacade.findAllBy(getFilter());
	}

	private Filter<MovimentacaoEstoque> getFilter() {
		return new QBEFilter<>(MovimentacaoEstoque.class);
	}

	public List<MovimentacaoEstoque> getMovimentacoes() {
		return movimentacoes;
	}

	public void setMovimentacoes(List<MovimentacaoEstoque> movimentacoes) {
		this.movimentacoes = movimentacoes;
	}

}
