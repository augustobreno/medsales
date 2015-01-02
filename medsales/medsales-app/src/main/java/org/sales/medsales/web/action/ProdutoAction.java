package org.sales.medsales.web.action;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.sales.medsales.dominio.Produto;
import org.sales.medsales.negocio.ProdutoFacade;

@SuppressWarnings("serial")
@Named
@ConversationScoped
public class ProdutoAction extends CrudActionBase<Produto, Long, ProdutoFacade>{

	@Inject
	private Conversation conversation;
	
	@Override
	@PostConstruct
	public void init() {
		super.init();
		conversation.begin();
		showInfoMessage("TESTE!");
	}
	
}
