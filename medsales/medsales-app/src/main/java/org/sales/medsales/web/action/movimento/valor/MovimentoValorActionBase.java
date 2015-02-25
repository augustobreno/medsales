package org.sales.medsales.web.action.movimento.valor;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Conversation;
import javax.enterprise.inject.Instance;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.easy.qbeasy.api.Filter;
import org.sales.medsales.api.exceptions.AppException;
import org.sales.medsales.api.negocio.CrudFacade;
import org.sales.medsales.api.web.action.CrudActionBase;
import org.sales.medsales.dominio.Ciclo;
import org.sales.medsales.dominio.movimento.valor.MovimentoValor;
import org.sales.medsales.negocio.movimentacao.valor.CicloFacade;

/**
 * Define o comportamento básico para o cadastro de movimentos em um ciclo.
 * @author Augusto
 */
@SuppressWarnings("serial")
public class MovimentoValorActionBase<ENTITY extends MovimentoValor, FACADE extends CrudFacade<ENTITY, Long>> 
	extends CrudActionBase<ENTITY, Long, FACADE>{

	/** CicloId: GET parameter para identificação de um ciclo que originou esta operação. */
	private Long clid;
	
	@Inject
	private Conversation conversation;
	
	@Inject
	private Instance<CicloFacade> cicloFacadeInstance;
	
	@Override
	@PostConstruct
	public void init() {
		super.init();
		if (conversation.isTransient()) {
			conversation.begin();
		}
	}

	@Override
	protected void configSearch(Filter<? extends ENTITY> filter) {
		super.configSearch(filter);
		filter.addFetch("ciclo", "parceiro");
	}
	
	@Override
	protected void initEntity() {
		super.initEntity();
		getEntity().setDataMovimento(new Date());
	}

	@Override
	protected void postPrepareToInsert() {
		super.postPrepareToInsert();
		
		// se este fluxo veio de um cadastro de ciclo, deverá haver um identificador para o clico
		if (clid != null) {
			Ciclo ciclo = getCicloFacade().findBy(clid, "investidor");
			getEntity().setCiclo(ciclo);
		}
	}
	
	@Override
	protected void postSave() {
		Long cicloId = getEntity().getCiclo().getId();
		super.postSave();
		returnToCiclo(cicloId);
	}

	@Override
	protected void postDelete(ENTITY entidade) {
		Long cicloId = getEntity().getCiclo().getId();
		super.postDelete(entidade);
		returnToCiclo(cicloId);
	}
	
	@Override
	public void cancelOperation() {
		Long cicloId = getEntity().getCiclo().getId();
		super.cancelOperation();
		returnToCiclo(cicloId);
	}
	
	/**
	 * Retorna ao fluxo do ciclo associado ao movimento no momento da persistência.
	 * @param cicloId ID do ciclo a retornar.
	 */
	private void returnToCiclo(Long cicloId) {
		/*
		 * Se a origem foi um Ciclo, então após a operação o fluxo deve ser direcionado de volta
		 */
		if (clid != null) {
			try {
			String link = "{0}/movimento/valor/ciclo/ciclo.xhtml?lid={1}";
				FacesContext.getCurrentInstance().getExternalContext()
						.redirect(MessageFormat.format(link, getAppWebContext(), cicloId));
			} catch (IOException e) {
				throw new AppException(e);
			}
		}
	}
	
	public Long getClid() {
		return clid;
	}

	public void setClid(Long clid) {
		this.clid = clid;
	}
	
	private CicloFacade getCicloFacade() {
		return this.cicloFacadeInstance.get();
	}
}
