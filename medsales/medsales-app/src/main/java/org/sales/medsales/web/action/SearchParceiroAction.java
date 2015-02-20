package org.sales.medsales.web.action;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.easy.qbeasy.QBEFilter;
import org.easy.qbeasy.api.Filter;
import org.sales.medsales.api.web.action.ActionBase;
import org.sales.medsales.dominio.Parceiro;
import org.sales.medsales.negocio.ParceiroFacade;

/**
 * Oferce suporte aos componentes de interface que realizam consulta de parceiros. 
 * @author Augusto
 */
@SuppressWarnings("serial")
@Named
public class SearchParceiroAction extends ActionBase {

	@Inject
	private ParceiroFacade parceiroFacade;
	
	/**
	 * Consulta por parceiros segundo a chave informada.
	 * @param chave
	 *            Chave para consulta de parceiros.
	 * @return Lista de parceiros segundo a chave informada.
	 */
	public List<Parceiro> search(String chave) {
		Filter<Parceiro> filter = new QBEFilter<Parceiro>(new Parceiro());
		filter.getExample().setNome(chave);
		return parceiroFacade.findAllBy(filter);
	}	
	
}
