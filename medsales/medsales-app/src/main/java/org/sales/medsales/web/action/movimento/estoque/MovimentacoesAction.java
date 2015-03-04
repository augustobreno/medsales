package org.sales.medsales.web.action.movimento.estoque;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.easy.qbeasy.api.Filter;
import org.easy.qbeasy.api.Operation;
import org.easy.qbeasy.api.operator.Operators;
import org.primefaces.event.SelectEvent;
import org.sales.medsales.api.web.action.ServerPaginationActionBased;
import org.sales.medsales.dominio.movimento.estoque.EntradaEstoque;
import org.sales.medsales.dominio.movimento.estoque.MovimentoEstoque;
import org.sales.medsales.dominio.movimento.estoque.SaidaEstoque;
import org.sales.medsales.negocio.movimentacao.estoque.EstoqueFacade;

/**
 * Mantém a tela inicial com listagem das movimentações cadastradas, contendo
 * direcionamento para outras telas para manutenção dos dados.
 * 
 * @author Augusto
 */
@SuppressWarnings("serial")
@Named
@ConversationScoped
public class MovimentacoesAction extends ServerPaginationActionBased<MovimentoEstoque, Long, EstoqueFacade> {

	@Inject
	private Conversation conversation;

	@Inject
	private EstoqueFacade estoqueFacade;

	/**
	 * Chave genérica para consulta.
	 */
	private String chaveConsulta;

	@PostConstruct
	public void init() {
		super.init();
		beginOrJoin(conversation);
	}

	@Override
	protected void configSearch(Filter<? extends MovimentoEstoque> filter) {
		super.configSearch(filter);

		filter.sortDescBy("dataMovimento", "id");
		filter.addFetch("parceiro", "itens.precoProduto");

		// TODO liberar este código quando distinct estiver aplicável à
		// consulta.
		if (getChaveConsulta() != null && !getChaveConsulta().trim().isEmpty()) {
			filter.addOr(new Operation("parceiro.nome", Operators.like(false), getChaveConsulta()), new Operation(
					"itens.produto.nome", Operators.like(false), getChaveConsulta()), new Operation(
					"itens.produto.codigoBarras", Operators.like(false), getChaveConsulta()));
		}
	}

	public void load(SelectEvent event) throws IOException {
		load((MovimentoEstoque) event.getObject());
	}
	
	public void load(MovimentoEstoque movimentacao) throws IOException {
		if (EntradaEstoque.class.isAssignableFrom(movimentacao.getClass())) {
			FacesContext.getCurrentInstance().getExternalContext().redirect("entrada.xhtml?lid=" + movimentacao.getId());
		} else 	if (SaidaEstoque.class.isAssignableFrom(movimentacao.getClass())) {
			FacesContext.getCurrentInstance().getExternalContext().redirect("saida.xhtml?lid=" + movimentacao.getId());
		} else {
			showErrorMessage("Não foi possível discernir o tipo da movimentação.");
		}
	}

	@Override
	public Class<MovimentoEstoque> getEntityType() {
		return MovimentoEstoque.class;
	}

	@Override
	protected EstoqueFacade getFacade() {
		return estoqueFacade;
	}

	public String getChaveConsulta() {
		return chaveConsulta;
	}

	public void setChaveConsulta(String chaveConsulta) {
		this.chaveConsulta = chaveConsulta;
	}

}
