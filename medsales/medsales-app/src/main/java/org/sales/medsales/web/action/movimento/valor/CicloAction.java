package org.sales.medsales.web.action.movimento.valor;

import java.io.IOException;
import java.text.MessageFormat;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.easy.qbeasy.api.Filter;
import org.primefaces.event.SelectEvent;
import org.sales.medsales.api.web.action.CrudActionBase;
import org.sales.medsales.api.web.action.CrudOperation;
import org.sales.medsales.dominio.Ciclo;
import org.sales.medsales.dominio.movimento.valor.EntradaEstoqueValor;
import org.sales.medsales.dominio.movimento.valor.Investimento;
import org.sales.medsales.dominio.movimento.valor.MovimentoValor;
import org.sales.medsales.dominio.movimento.valor.Valor;
import org.sales.medsales.negocio.movimentacao.valor.CicloFacade;

@SuppressWarnings("serial")
@Named
@ConversationScoped
public class CicloAction extends CrudActionBase<Ciclo, Long, CicloFacade> {

	private static final String INVESTIMENTO_LINK = "/movimento/valor/investimento/investimento.xhtml";
	private static final String VALOR_LINK = "/movimento/valor/valor/valor.xhtml";
	private static final String ENTRADA_ESTOQUE_LINK = "/movimento/valor/entradaEstoque/entradaEstoque.xhtml";
	
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

	@Override
	protected void configSearch(Filter<? extends Ciclo> filter) {
		super.configSearch(filter);

		filter.addFetch("investidor", "movimentos.parceiro");
	}

	@Override
	protected void postSave() {
		/*
		 * Após salvar um ciclo, se este foi um fluxo de inclusão, então mantém
		 * o usuário nesta mesma página.
		 */
		if (CrudOperation.INSERT.equals(getCrudOperation())) {
			showInfoMessage(getSaveSuccessMessage());
		} else {
			super.postSave();
		}

	}

	public void load(SelectEvent event) throws IOException {
		load((MovimentoValor) event.getObject());
	}

	/**
	 * Carrega um movimento para edição. 
	 */
	public void load(MovimentoValor movimento) throws IOException {
		
		String link = null;
		if (Investimento.class.isAssignableFrom(movimento.getClass())) {
			link = INVESTIMENTO_LINK;
		} else if (Valor.class.isAssignableFrom(movimento.getClass())) {
			link = VALOR_LINK;
		} else if (EntradaEstoqueValor.class.isAssignableFrom(movimento.getClass())) {
			link = ENTRADA_ESTOQUE_LINK;
		} 
		
		if (link == null) {
			showErrorMessage("Não foi possível discernir o tipo do movimento.");
		} else {
			String linkComParamentros = "{0}" + link + "?lid={1}&clid={2}";
			FacesContext.getCurrentInstance().getExternalContext()
					.redirect(MessageFormat.format(linkComParamentros, getAppWebContext(), movimento.getId(), getEntity().getId()));
		}
	}

	public String getInvestimentoLink() throws IOException {
		String link = INVESTIMENTO_LINK;
		return montarLinkParaNovoMovimento(link);
	}
	
	public String getValorLink() throws IOException {
		String link = VALOR_LINK;
		return montarLinkParaNovoMovimento(link);
	}
	
	public String getEstradaEstoqueValorLink() throws IOException {
		String link = ENTRADA_ESTOQUE_LINK;
		return montarLinkParaNovoMovimento(link);
	}

	private String montarLinkParaNovoMovimento(String link) {
		String linkComParamentros = link + "?op={0}&clid={1}&{2}"; 
		return MessageFormat.format(linkComParamentros, CrudOperation.INSERT.getOperation(), getEntity().getId(), "faces-redirect=true");
	}
}
