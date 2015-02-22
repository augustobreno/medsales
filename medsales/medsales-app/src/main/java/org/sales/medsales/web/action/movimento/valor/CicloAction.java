package org.sales.medsales.web.action.movimento.valor;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.easy.qbeasy.api.Filter;
import org.sales.medsales.api.web.action.CrudActionBase;
import org.sales.medsales.dominio.Ciclo;
import org.sales.medsales.negocio.movimentacao.valor.CicloFacade;

@SuppressWarnings("serial")
@Named
@ConversationScoped
public class CicloAction extends CrudActionBase<Ciclo, Long, CicloFacade>{

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
}