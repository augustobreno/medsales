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
import org.sales.medsales.dominio.movimento.valor.Investimento;
import org.sales.medsales.dominio.movimento.valor.MovimentoValor;
import org.sales.medsales.negocio.movimentacao.valor.CicloFacade;

@SuppressWarnings("serial")
@Named
@ConversationScoped
public class CicloAction extends CrudActionBase<Ciclo, Long, CicloFacade> {

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

	public void load(MovimentoValor movimento) throws IOException {
		if (Investimento.class.isAssignableFrom(movimento.getClass())) {
			String link = "{0}/movimento/valor/investimento/investimento.xhtml?lid={1}&clid={2}";
			FacesContext.getCurrentInstance().getExternalContext()
					.redirect(MessageFormat.format(link, getAppWebContext(), movimento.getId(), getEntity().getId()));
		} else {
			showErrorMessage("Não foi possível discernir o tipo do movimento.");
		}
	}

	public String getInvestimentoLink() throws IOException {
		String link = "/movimento/valor/investimento/investimento.xhtml?op={0}&clid={1}&{2}";
		return MessageFormat.format(link, CrudOperation.INSERT.getOperation(), getEntity().getId(), "faces-redirect=true");
	}
}
