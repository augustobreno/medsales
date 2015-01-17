package org.sales.medsales.web.action.movimentacao;

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
import org.sales.medsales.dominio.movimentacao.Entrada;
import org.sales.medsales.dominio.movimentacao.MovimentacaoEstoque;
import org.sales.medsales.dominio.movimentacao.Saida;
import org.sales.medsales.negocio.movimentacao.EstoqueFacade;

/**
 * Mantém a tela inicial com listagem das movimentações cadastradas, contendo
 * direcionamento para outras telas para manutenção dos dados.
 * 
 * @author Augusto
 */
@SuppressWarnings("serial")
@Named
@ConversationScoped
public class MovimentacoesAction extends ServerPaginationActionBased<MovimentacaoEstoque, Long, EstoqueFacade> {

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
	protected void configSearch(Filter<? extends MovimentacaoEstoque> filter) {
		super.configSearch(filter);

		filter.sortDescBy("dataMovimentacao");
		filter.addFetch("parceiro");

		// TODO liberar este código quando distinct estiver aplicável à
		// consulta.
		if (getChaveConsulta() != null && !getChaveConsulta().trim().isEmpty()) {
			filter.addOr(new Operation("parceiro.nome", Operators.like(false), getChaveConsulta()), new Operation(
					"itens.produto.nome", Operators.like(false), getChaveConsulta()), new Operation(
					"itens.produto.codigoBarras", Operators.like(false), getChaveConsulta()));
		}
	}

	public void load(SelectEvent event) throws IOException {
		load((MovimentacaoEstoque) event.getObject());
	}
	
	public void load(MovimentacaoEstoque movimentacao) throws IOException {
		if (Entrada.class.isAssignableFrom(movimentacao.getClass())) {
			FacesContext.getCurrentInstance().getExternalContext().redirect("entrada.xhtml?lid=" + movimentacao.getId());
		} else 	if (Saida.class.isAssignableFrom(movimentacao.getClass())) {
			FacesContext.getCurrentInstance().getExternalContext().redirect("saida.xhtml?lid=" + movimentacao.getId());
		} else {
			showErrorMessage("Não foi possível dicernir o tipo da movimentação.");
		}
	}

	@Override
	public Class<MovimentacaoEstoque> getEntityType() {
		return MovimentacaoEstoque.class;
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
