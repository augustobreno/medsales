package org.sales.medsales.web.action.movimento.valor;

import java.util.Date;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.easy.qbeasy.api.Filter;
import org.sales.medsales.api.web.action.CrudActionBase;
import org.sales.medsales.dominio.movimento.valor.Investimento;
import org.sales.medsales.negocio.movimentacao.valor.InvestimentoFacade;

@SuppressWarnings("serial")
@Named
@ConversationScoped
public class InvestimentoAction extends CrudActionBase<Investimento, Long, InvestimentoFacade>{

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
	protected void configSearch(Filter<? extends Investimento> filter) {
		super.configSearch(filter);
		filter.addFetch("ciclo", "parceiro");
	}
	
	@Override
	protected void initEntity() {
		super.initEntity();
		getEntity().setDataMovimento(new Date());
	}
}
